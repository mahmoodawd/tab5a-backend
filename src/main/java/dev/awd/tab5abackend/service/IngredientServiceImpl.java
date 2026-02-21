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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final UploadService uploadService;
    private final IngredientMapper ingredientMapper;

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
}
