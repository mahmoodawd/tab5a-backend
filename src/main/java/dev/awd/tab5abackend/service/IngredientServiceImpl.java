package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.ImageType;
import dev.awd.tab5abackend.dto.request.IngredientRequestDto;
import dev.awd.tab5abackend.dto.response.IngredientResponseDto;
import dev.awd.tab5abackend.exception.IngredientAlreadyExistException;
import dev.awd.tab5abackend.exception.IngredientNotFoundException;
import dev.awd.tab5abackend.mapper.IngredientMapper;
import dev.awd.tab5abackend.model.Ingredient;
import dev.awd.tab5abackend.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final UploadService uploadService;
    private final IngredientMapper ingredientMapper;

    @Override
    public List<IngredientResponseDto> findAll() {
        return ingredientRepository
                .findAll()
                .stream()
                .map(ingredientMapper::ingredientToIngredientResponseDto)
                .toList();
    }

    @Override
    public IngredientResponseDto findById(Long id) {
        return ingredientRepository
                .findById(id).map(ingredientMapper::ingredientToIngredientResponseDto)
                .orElseThrow(() -> new IngredientNotFoundException(id));
    }

    @Override
    public IngredientResponseDto save(IngredientRequestDto ingredientRequest) throws IngredientAlreadyExistException {
        if (ingredientRepository.existsByTitle(ingredientRequest.getTitle()))
            throw new IngredientAlreadyExistException(ingredientRequest.getTitle());
        Ingredient ingredientToSave = ingredientMapper.ingredientRequestDtoToIngredient(ingredientRequest);
        String imageStoragePath = uploadService.uploadImage(ingredientRequest.getImage(), ImageType.INGREDIENT);
        ingredientToSave.setImagePath(imageStoragePath);
        Ingredient savedIngredient = ingredientRepository.save(ingredientToSave);
        return ingredientMapper.ingredientToIngredientResponseDto(savedIngredient);
    }
}
