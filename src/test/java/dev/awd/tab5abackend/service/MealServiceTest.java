package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.request.MealRequestDto;
import dev.awd.tab5abackend.dto.response.MealResponseDto;
import dev.awd.tab5abackend.exception.MealAlreadyExistException;
import dev.awd.tab5abackend.exception.MealNotFoundException;
import dev.awd.tab5abackend.mapper.MealMapper;
import dev.awd.tab5abackend.model.Meal;
import dev.awd.tab5abackend.repository.MealRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

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
    void MealService_CreateNewMealWithValidInfo_MealCreated() {
        Meal meal = createMeal(1L, "meal1");
        MockMultipartFile image = new MockMultipartFile("image", "test.png", "image/png", "fake image".getBytes());
        MealRequestDto MealRequest = MealRequestDto.builder().title("ing1").image(image).build();
        MealResponseDto expectedDto = createMealResponse(1L, "meal1");

        when(mealMapper.mealRequestDtoToMeal(any(MealRequestDto.class))).thenReturn(meal);
        when(mealRepository.save(any(Meal.class))).thenReturn(meal);
        when(mealMapper.mealToMealResponseDto(any(Meal.class))).thenReturn(expectedDto);

        MealResponseDto responseDto = mealService.save(MealRequest);

        assertNotNull(responseDto);
        assertEquals(responseDto.getId(), 1L);
    }

    @SneakyThrows
    @Test
    void MealService_CreateNewMealWithExistingTitle_ThrowsException() {
        MockMultipartFile image = new MockMultipartFile("image", "test.png", "image/png", "fake image".getBytes());
        MealRequestDto MealRequest = MealRequestDto.builder().title("ing1").image(image).build();

        when(mealRepository.existsByTitle(anyString())).thenReturn(true);

        assertThrows(MealAlreadyExistException.class, () -> mealService.save(MealRequest));

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
                .build();
    }
}