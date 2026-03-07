package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.request.IngredientRequestDto;
import dev.awd.tab5abackend.dto.response.IngredientResponseDto;
import dev.awd.tab5abackend.exception.IngredientAlreadyExistException;
import dev.awd.tab5abackend.exception.IngredientNotFoundException;
import dev.awd.tab5abackend.mapper.IngredientMapper;
import dev.awd.tab5abackend.model.Ingredient;
import dev.awd.tab5abackend.repository.IngredientRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @InjectMocks
    private IngredientServiceImpl ingredientService;
    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private IngredientMapper ingredientMapper;

    @Test
    void IngredientService_FindAll_ReturnsAllIngredients() {
        List<Ingredient> IngredientList = List.of(
                new Ingredient(1L, "ing1", "images/ing1.png"),
                new Ingredient(2L, "ing2", "images/ing2.png")

        );
        when(ingredientRepository.findAll()).thenReturn(IngredientList);
        List<IngredientResponseDto> allIngredients = ingredientService.findAll();
        assertNotNull(allIngredients);
        assertFalse(allIngredients.isEmpty());
        assertEquals(IngredientList.size(), 2);
    }

    @Test
    void IngredientService_FindById_ReturnsIngredientResponseDto() {
        Ingredient ingredient = new Ingredient(1L, "ing1", "images/ing1.png");
        IngredientResponseDto expectedDto = new IngredientResponseDto(1L, "ing1", "images/ing1.png");

        when(ingredientRepository.findById(anyLong())).thenReturn(Optional.of(ingredient));
        when(ingredientMapper.ingredientToIngredientResponseDto(ingredient)).thenReturn(expectedDto);
        IngredientResponseDto responseDto = ingredientService.findById(1L);
        assertNotNull(responseDto);
        assertEquals(responseDto.getTitle(), ingredient.getTitle());
    }

    @Test
    void IngredientService_FindByNonExistingId_ThrowsException() {
        when(ingredientRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IngredientNotFoundException.class, () -> ingredientService.findById(1L));
    }

    @SneakyThrows
    @Test
    void IngredientService_CreateNewIngredientWithValidInfo_IngredientCreated() {
        Ingredient ingredient = new Ingredient(1L, "ing1", "images/ing1.png");
        MockMultipartFile image = new MockMultipartFile("image", "test.png", "image/png", "fake image".getBytes());
        IngredientRequestDto IngredientRequest = new IngredientRequestDto("ing1", image);
        IngredientResponseDto expectedDto = new IngredientResponseDto(1L, "ing1", "images/ing1.png");

        when(ingredientMapper.ingredientRequestDtoToIngredient(any(IngredientRequestDto.class))).thenReturn(ingredient);
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);
        when(ingredientMapper.ingredientToIngredientResponseDto(any(Ingredient.class))).thenReturn(expectedDto);

        IngredientResponseDto responseDto = ingredientService.save(IngredientRequest);

        assertNotNull(responseDto);
        assertEquals(responseDto.getId(), 1L);
    }

    @SneakyThrows
    @Test
    void IngredientService_CreateNewIngredientWithExistingTitle_ThrowsException() {
        MockMultipartFile image = new MockMultipartFile("image", "test.png", "image/png", "fake image".getBytes());
        IngredientRequestDto IngredientRequest = new IngredientRequestDto("ing1", image);

        when(ingredientRepository.existsByTitle(anyString())).thenReturn(true);

        assertThrows(IngredientAlreadyExistException.class, () -> ingredientService.save(IngredientRequest));

    }

}