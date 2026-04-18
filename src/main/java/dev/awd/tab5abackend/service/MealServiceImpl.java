package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.ImageType;
import dev.awd.tab5abackend.dto.request.MealIngredientRequestDto;
import dev.awd.tab5abackend.dto.request.MealRequestDto;
import dev.awd.tab5abackend.dto.response.CommentResponseDto;
import dev.awd.tab5abackend.dto.response.MealIngredientResponseDto;
import dev.awd.tab5abackend.dto.response.MealResponseDto;
import dev.awd.tab5abackend.exception.*;
import dev.awd.tab5abackend.mapper.CommentMapper;
import dev.awd.tab5abackend.mapper.MealIngredientMapper;
import dev.awd.tab5abackend.mapper.MealMapper;
import dev.awd.tab5abackend.model.*;
import dev.awd.tab5abackend.repository.*;
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
    private final MealIngredientMapper mealIngredientMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final CategoryRepository categoryRepository;
    private final ChefRepository chefRepository;


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
                .map(response -> {
                    log.debug("Fetching ingredients for meal: {}", id);
                    List<MealIngredientResponseDto> ingredients = mealIngredientRepository
                            .findAllByMealId(id)
                            .stream()
                            .map(mealIngredientMapper::toResponseDto)
                            .toList();

                    log.debug("Fetching comments for meal: {}", id);
                    List<CommentResponseDto> comments = commentRepository
                            .findAllByMealId(id)
                            .stream()
                            .map(commentMapper::commentToCommentResponseDto)
                            .toList();

                    response.setIngredients(ingredients);
                    response.setComments(comments);
                    return response;
                })
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
            Category category = categoryRepository.findById(mealRequestDto.getCategoryId()).orElseThrow(() ->
                    new CategoryNotFoundException(mealRequestDto.getCategoryId()));

            Chef chef = chefRepository.findById(mealRequestDto.getChefId()).orElseThrow(() -> new ChefNotFoundException(mealRequestDto.getChefId()));

            log.debug("Mapping DTO to entity for meal: '{}'", title);
            Meal mealToSave = mealMapper.mealRequestDtoToMeal(mealRequestDto);

            mealToSave.setCategory(category);
            mealToSave.setChef(chef);

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

    private void insertIngredients(Meal meal, List<MealIngredientRequestDto> ingredientsMeasures) {
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