package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.ImageType;
import dev.awd.tab5abackend.dto.request.ChefRequestDto;
import dev.awd.tab5abackend.dto.response.ChefResponseDto;
import dev.awd.tab5abackend.exception.ChefAlreadyExistException;
import dev.awd.tab5abackend.exception.ChefNotFoundException;
import dev.awd.tab5abackend.mapper.ChefMapper;
import dev.awd.tab5abackend.model.Chef;
import dev.awd.tab5abackend.repository.ChefRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChefServiceImpl implements ChefService {

    private final ChefRepository chefRepository;
    private final UploadService uploadService;
    private final ChefMapper chefMapper;


    @Override
    public List<ChefResponseDto> findAll() {
        log.debug("Fetching all chefs");

        List<ChefResponseDto> chefs = chefRepository
                .findAll()
                .stream()
                .map(chefMapper::chefToChefResponseDto)
                .toList();

        log.info("Retrieved {} chefs", chefs.size());
        return chefs;
    }

    @Override
    public ChefResponseDto findById(Long id) {
        log.debug("Fetching chef with id: {}", id);

        return chefRepository.findById(id)
                .map(chefMapper::chefToChefResponseDto)
                .orElseThrow(() -> {
                    log.warn("chef not found with id: {}", id);
                    return new ChefNotFoundException(id);
                });
    }

    @Override
    public ChefResponseDto save(ChefRequestDto chefRequest) throws ChefAlreadyExistException {
        log.info("Registering New Chef: '{}'", chefRequest.getName());

        if (chefRepository.existsByName(chefRequest.getName())) {
            log.warn("Chef registration failed - duplicate name: '{}' ", chefRequest.getName());
            throw new ChefAlreadyExistException(chefRequest.getName());
        }

        log.debug("Mapping chef DTO to entity");
        Chef chefToSave = chefMapper.chefRequestDtoToChef(chefRequest);

        log.debug("Uploading chef image");
        String imageStoragePath = uploadService.uploadImage(chefRequest.getAvatar(), ImageType.CHEF);
        chefToSave.setAvatar(imageStoragePath);

        log.debug("Saving chef to DB");
        Chef savedChef = chefRepository.save(chefToSave);

        log.info("Chef created successfully: id={}, name='{}', avatarPath={}",
                savedChef.getId(), savedChef.getName(), imageStoragePath);

        log.debug("Mapping chef entity to response DTO");
        return chefMapper.chefToChefResponseDto(savedChef);
    }
}
