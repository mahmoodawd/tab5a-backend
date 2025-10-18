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
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChefServiceImpl implements ChefService {

    private final ChefRepository chefRepository;
    private final UploadService uploadService;
    private final ChefMapper chefMapper;


    @Override
    public List<ChefResponseDto> findAll() {
        return chefRepository
                .findAll()
                .stream()
                .map(chefMapper::chefToChefResponseDto)
                .toList();
    }

    @Override
    public ChefResponseDto findById(Long id) {
        return chefRepository.findById(id)
                .map(chefMapper::chefToChefResponseDto)
                .orElseThrow(() -> new ChefNotFoundException(id));
    }

    @Override
    public ChefResponseDto save(ChefRequestDto chefRequest) throws ChefAlreadyExistException {
        if (chefRepository.existsByName(chefRequest.getName()))
            throw new ChefAlreadyExistException(chefRequest.getName());
        Chef chefToSave = chefMapper.chefRequestDtoToChef(chefRequest);
        String imageStoragePath = uploadService.uploadImage(chefRequest.getAvatar(), ImageType.CHEF);
        chefToSave.setAvatar(imageStoragePath);
        Chef savedChef = chefRepository.save(chefToSave);
        return chefMapper.chefToChefResponseDto(savedChef);
    }
}
