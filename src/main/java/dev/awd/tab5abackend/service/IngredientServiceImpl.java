package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.ImageType;
import dev.awd.tab5abackend.dto.request.IngredientRequestDto;
import dev.awd.tab5abackend.dto.request.MealIngredientRequestDto;
import dev.awd.tab5abackend.dto.response.IngredientResponseDto;
import dev.awd.tab5abackend.dto.response.MealIngredientResponseDto;
import dev.awd.tab5abackend.exception.IngredientAlreadyExistException;
import dev.awd.tab5abackend.exception.IngredientNotFoundException;
import dev.awd.tab5abackend.mapper.IngredientMapper;
import dev.awd.tab5abackend.mapper.MealIngredientMapper;
import dev.awd.tab5abackend.model.Ingredient;
import dev.awd.tab5abackend.model.Meal;
import dev.awd.tab5abackend.model.MealIngredient;
import dev.awd.tab5abackend.model.MealIngredientId;
import dev.awd.tab5abackend.repository.IngredientRepository;
import dev.awd.tab5abackend.repository.MealIngredientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;
    private final MealIngredientMapper mealIngredientMapper;
    private final MealIngredientRepository mealIngredientRepository;

    private final UploadService uploadService;

    @Override
    public List<IngredientResponseDto> findAll() {
        log.debug("Fetching all ingredients");

        List<IngredientResponseDto> ingredients = ingredientRepository
                .findAll()
                .stream()
                .map(ingredientMapper::ingredientToIngredientResponseDto)
                .toList();

        log.info("Retrieved {} ingredients", ingredients.size());
        return ingredients;
    }

    @Override
    public IngredientResponseDto findById(Long id) {
        log.info("Fetching ingredient with id: {}", id);

        return ingredientRepository
                .findById(id).map(ingredientMapper::ingredientToIngredientResponseDto)
                .orElseThrow(() -> {
                    log.warn("Ingredient not found with id: {}", id);
                    return new IngredientNotFoundException(id);
                });
    }

    @Override
    public IngredientResponseDto save(IngredientRequestDto ingredientRequest) throws IngredientAlreadyExistException {
        String title = ingredientRequest.getTitle();

        log.info("Creating new Ingredient: '{}'", title);

        if (ingredientRepository.existsByTitle(title)) {
            log.warn("Ingredient creation failed - duplicate title: '{}'", title);
            throw new IngredientAlreadyExistException(ingredientRequest.getTitle());
        }

        log.debug("Mapping ingredient DTO to entity");
        Ingredient ingredientToSave = ingredientMapper.ingredientRequestDtoToIngredient(ingredientRequest);

        log.debug("Uploading ingredient image");
        String imageStoragePath = uploadService.uploadImage(ingredientRequest.getImage(), ImageType.INGREDIENT);
        ingredientToSave.setImagePath(imageStoragePath);

        log.debug("saving ingredient to database");
        Ingredient savedIngredient = ingredientRepository.save(ingredientToSave);

        log.info("Ingredient created successfully: id={}, title='{}', imagePath={}",
                savedIngredient.getId(), title, imageStoragePath);

        log.debug("Mapping ingredient entity to response DTO");
        return ingredientMapper.ingredientToIngredientResponseDto(savedIngredient);
    }

    @Override
    public Ingredient findEntityByTitle(String title) {
        return ingredientRepository
                .findOneByTitle(title)
                .orElseThrow(() -> {
                    log.warn("Ingredient not found with title: {}", title);
                    return new IngredientNotFoundException(title);
                });

    }

    @Override
    public List<MealIngredientResponseDto> getMealIngredients(Long mealId) {
        log.info("Fetching Meal [{}] ingredients", mealId);
        List<MealIngredientResponseDto> ingredients = mealIngredientRepository
                .findAllByMealId(mealId)
                .stream()
                .map(mealIngredientMapper::toResponseDto)
                .toList();
        log.info("Retrieved {} ingredients", ingredients.size());

        return ingredients;

    }

    @Override
    public void insertIngredients(Meal meal, List<MealIngredientRequestDto> ingredientsMeasures) {
        List<MealIngredient> mealIngredients = new ArrayList<>();
        log.debug("Preparing ingredients list");
        ingredientsMeasures.forEach(item -> {
            Ingredient ingredient = findEntityByTitle(item.getIngredient().toLowerCase());

            MealIngredient mealIngredient = new MealIngredient();
            mealIngredient.setId(new MealIngredientId(meal.getId(), ingredient.getId()));
            mealIngredient.setMeal(meal);
            mealIngredient.setIngredient(ingredient);
            mealIngredient.setMeasure(item.getMeasure());
            mealIngredients.add(mealIngredient);
        });
        log.debug("Ingredients List prepared, inserting.....");
        mealIngredientRepository.saveAll(mealIngredients);
    }
}
