package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.ImageType;
import dev.awd.tab5abackend.dto.request.MealIngredientDto;
import dev.awd.tab5abackend.dto.request.MealRequestDto;
import dev.awd.tab5abackend.dto.response.MealResponseDto;
import dev.awd.tab5abackend.exception.IngredientNotFoundException;
import dev.awd.tab5abackend.exception.MealAlreadyExistException;
import dev.awd.tab5abackend.exception.MealCreationException;
import dev.awd.tab5abackend.exception.MealNotFoundException;
import dev.awd.tab5abackend.mapper.MealMapper;
import dev.awd.tab5abackend.model.Ingredient;
import dev.awd.tab5abackend.model.Meal;
import dev.awd.tab5abackend.model.MealIngredient;
import dev.awd.tab5abackend.model.MealIngredientId;
import dev.awd.tab5abackend.repository.IngredientRepository;
import dev.awd.tab5abackend.repository.MealIngredientRepository;
import dev.awd.tab5abackend.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final UploadService uploadService;
    private final MealMapper mealMapper;
    private final IngredientRepository ingredientRepository;
    private final MealIngredientRepository mealIngredientRepository;


    @Override
    @Transactional(readOnly = true)
    public List<MealResponseDto> findAll() {
        log.debug("Fetching all meals from database");

        List<MealResponseDto> meals = mealRepository.findAll()
                .stream()
                .map(mealMapper::mealToMealResponseDto)
                .toList();

        log.info("Retrieved {} meals", meals.size());
        return meals;
    }

    @Override
    @Transactional(readOnly = true)
    public MealResponseDto findById(Long id) {
        log.debug("Fetching meal with id: {}", id);

        return mealRepository.findById(id)
                .map(mealMapper::mealToMealResponseDto)
                .orElseThrow(() -> {
                    log.error("Meal not found with id: {}", id);
                    return new MealNotFoundException(id);
                });
    }

    @Transactional
    @Override
    public MealResponseDto save(MealRequestDto mealRequestDto) throws MealAlreadyExistException, MealCreationException {
        String title = mealRequestDto.getTitle();

        log.info("Creating new meal: '{}' ", title);

        if (mealRepository.existsByTitle(title)) {
            log.warn("Attempt to create duplicate meal: '{}'", title);
            throw new MealAlreadyExistException(title);
        }

        try {
            log.debug("Mapping DTO to entity for meal: '{}'", title);
            Meal mealToSave = mealMapper.mealRequestDtoToMeal(mealRequestDto);

            log.debug("Uploading image for meal: '{}'", title);
            String imageStoragePath = uploadService.uploadImage(
                    mealRequestDto.getImage(),
                    ImageType.MEAL
            );
            mealToSave.setImagePath(imageStoragePath);
            log.debug("Image uploaded to path: {}", imageStoragePath);

            Meal savedMeal = mealRepository.save(mealToSave);

            log.info("Inserting Ingredients...");
            insertIngredients(savedMeal, mealRequestDto.getIngredients());

            log.info("Meal created successfully: id={}, title='{}', imagePath={}",
                    savedMeal.getId(), title, imageStoragePath);
            return mealMapper.mealToMealResponseDto(savedMeal);

        } catch (Exception e) {
            log.error("Failed to create meal: '{}'. Error: {}", title, e.getMessage());
            throw new MealCreationException("Failed to create meal: '" + title + "': " + e.getMessage());
        }
    }

    private void insertIngredients(Meal meal, List<MealIngredientDto> ingredientsMeasures) {
        List<MealIngredient> mealIngredients = new ArrayList<>();
            log.debug("Preparing ingredients list");
            ingredientsMeasures.forEach(item -> {
                Ingredient ingredient = ingredientRepository.findOneByTitle(item.getIngredient().toLowerCase())
                        .orElseThrow(() -> {
                            log.error("Non existing ingredient: {}", item.getIngredient());
                            return new IngredientNotFoundException(item.getIngredient());
                        });
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