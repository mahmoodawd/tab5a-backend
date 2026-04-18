package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.request.MealIngredientDto;
import dev.awd.tab5abackend.dto.request.MealRequestDto;
import dev.awd.tab5abackend.dto.response.MealIngredientResponseDto;
import dev.awd.tab5abackend.dto.response.MealResponseDto;
import dev.awd.tab5abackend.exception.MealAlreadyExistException;
import dev.awd.tab5abackend.exception.MealCreationException;
import dev.awd.tab5abackend.exception.MealNotFoundException;
import dev.awd.tab5abackend.mapper.MealMapper;
import dev.awd.tab5abackend.model.Ingredient;
import dev.awd.tab5abackend.model.Meal;
import dev.awd.tab5abackend.repository.IngredientRepository;
import dev.awd.tab5abackend.repository.MealIngredientRepository;
import dev.awd.tab5abackend.repository.MealRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealServiceTest {

    @InjectMocks
    private MealServiceImpl mealService;
    @Mock
    private MealRepository mealRepository;
    @Mock
    private MealMapper mealMapper;
    @Mock
    private UploadService uploadService;
    @Mock
    private MealIngredientRepository mealIngredientRepository;
    @Mock
    private IngredientRepository ingredientRepository;

    @Test
    void MealService_FindAll_ReturnsAllMeals() {
        List<Meal> MealList = List.of(
                createMeal(1L, "meal1"),
                createMeal(2L, "meal2")

        );
        when(mealRepository.findAll()).thenReturn(MealList);
        List<MealResponseDto> allMeals = mealService.findAll();
        assertNotNull(allMeals);
        assertFalse(allMeals.isEmpty());
        assertEquals(MealList.size(), 2);
    }

    @Test
    void MealService_FindById_ReturnsMealResponseDto() {
        Meal meal = createMeal(1L, "meal1");
        MealResponseDto expectedDto = createMealResponse(1L, "meal1");

        when(mealRepository.findById(anyLong())).thenReturn(Optional.of(meal));
        when(mealMapper.mealToMealResponseDto(meal)).thenReturn(expectedDto);
        MealResponseDto responseDto = mealService.findById(1L);
        assertNotNull(responseDto);
        assertEquals(responseDto.getTitle(), meal.getTitle());
    }

    @Test
    void MealService_FindByNonExistingId_ThrowsException() {
        when(mealRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(MealNotFoundException.class, () -> mealService.findById(1L));
    }


    @SneakyThrows
    @Test
    void MealService_CreateNewMealWithExistingTitle_ThrowsException() {
        MockMultipartFile image = new MockMultipartFile("image", "test.png", "image/png", "fake image".getBytes());
        MealRequestDto existingMealRequest = MealRequestDto.builder().title("existingMeal").image(image).build();

        when(mealRepository.existsByTitle(anyString())).thenReturn(true);

        assertThrows(MealAlreadyExistException.class, () -> mealService.save(existingMealRequest));

    }

    @SneakyThrows
    @Test
    void MealService_CreateNewMealWithValidInfo_MealCreated() {
        Meal meal = createMeal(1L, "meal1");
        MockMultipartFile image = new MockMultipartFile("image", "test.png", "image/png", "fake image".getBytes());
        MealRequestDto MealRequest = MealRequestDto.builder().title("meal1").image(image)
                .ingredients(getIngredients())
                .build();
        MealResponseDto expectedDto = createMealResponse(1L, "meal1");

        when(mealMapper.mealRequestDtoToMeal(any(MealRequestDto.class))).thenReturn(meal);
        when(mealRepository.save(any(Meal.class))).thenReturn(meal);
        when(ingredientRepository.findOneByTitle(anyString())).thenReturn(Optional.of(createIngredient()));
        when(mealMapper.mealToMealResponseDto(any(Meal.class))).thenReturn(expectedDto);

        MealResponseDto responseDto = mealService.save(MealRequest);

        assertNotNull(responseDto);
        assertEquals(responseDto.getId(), 1L);
        verify(mealIngredientRepository, times(1)).saveAll(any());

    }

    @SneakyThrows
    @Test
    void MealService_CreateNewMealWithNonExistingIngredients_ThrowsException() {

        Meal meal = createMeal(1L, "newMeal");
        MockMultipartFile image = new MockMultipartFile("image", "test.png", "image/png", "fake image".getBytes());
        MealRequestDto mealRequest = MealRequestDto.builder()
                .title("newMeal")
                .image(image)
                .ingredients(getIngredients())
                .build();

        when(mealMapper.mealRequestDtoToMeal(mealRequest)).thenReturn(meal);
        when(mealRepository.save(any(Meal.class))).thenReturn(meal);
        when(ingredientRepository.findOneByTitle(anyString())).thenReturn(Optional.empty());

        assertThrows(MealCreationException.class, () -> mealService.save(mealRequest));
    }

    private Meal createMeal(long id, String title) {
        return Meal.builder()
                .id(id)
                .title(title)
                .addedAt(Instant.now())
                .imagePath("images/" + title + ".png")
                .description("Description of: " + title)
                .build();
    }

    private MealResponseDto createMealResponse(long id, String title) {
        return MealResponseDto.builder()
                .id(id)
                .title(title)
                .addedAt(Instant.now())
                .imageUrl("api/images/" + title + ".png")
                .description("Description of: " + title)
                .ingredients(createMealIngredients())
                .build();
    }

    private List<MealIngredientDto> getIngredients() {
        return List.of(
                new MealIngredientDto("Tomato", "1 Piece"),
                new MealIngredientDto("macaroni", "2 Cups"),
                new MealIngredientDto("garlic", "4 Cloves")
        );
    }

    private Ingredient createIngredient() {
        return new Ingredient(1L, "Tomato", "");
    }

    private List<MealIngredientResponseDto> createMealIngredients() {
        Map<String, String> ingredients = Map.of("Tomato",
                "1 Piece",
                "macaroni",
                "2 Cups",
                "garlic",
                "4 Cloves");

        return Collections.emptyList();
    }
}