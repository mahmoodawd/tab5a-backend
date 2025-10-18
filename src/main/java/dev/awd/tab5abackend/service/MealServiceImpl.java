package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.ImageType;
import dev.awd.tab5abackend.dto.request.MealRequestDto;
import dev.awd.tab5abackend.dto.response.MealResponseDto;
import dev.awd.tab5abackend.exception.MealAlreadyExistException;
import dev.awd.tab5abackend.exception.MealNotFoundException;
import dev.awd.tab5abackend.mapper.MealMapper;
import dev.awd.tab5abackend.model.Meal;
import dev.awd.tab5abackend.repository.IngredientRepository;
import dev.awd.tab5abackend.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MealServiceImpl implements MealService {
    private final MealRepository mealRepository;
    private final UploadService uploadService;
    private final MealMapper mealMapper;
    private final IngredientRepository ingredientRepository;

    @Override
    public List<MealResponseDto> findAll() {
        return mealRepository
                .findAll()
                .stream()
                .map(mealMapper::mealToMealResponseDto)
                .toList();
    }

    @Override
    public MealResponseDto findById(Long id) {
        return mealRepository
                .findById(id)
                .map(mealMapper::mealToMealResponseDto)
                .orElseThrow(() -> new MealNotFoundException(id));
    }

    @Override
    public MealResponseDto save(MealRequestDto mealRequestDto) throws MealAlreadyExistException {
        if (ingredientRepository.existsByTitle(mealRequestDto.getTitle()))
            throw new MealAlreadyExistException(mealRequestDto.getTitle());

        Meal mealToSave = mealMapper.mealRequestDtoToMeal(mealRequestDto);
        String imageStoragePath = uploadService.uploadImage(mealRequestDto.getImage(), ImageType.MEAL);
        mealToSave.setImagePath(imageStoragePath);
        Meal savedMeal = mealRepository.save(mealToSave);
        return mealMapper.mealToMealResponseDto(savedMeal);
    }

    @Override
    public void addRating(Long id, Double rating) {

    }
}
