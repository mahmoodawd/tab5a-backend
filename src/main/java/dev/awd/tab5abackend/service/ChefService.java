package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.request.ChefRequestDto;
import dev.awd.tab5abackend.dto.response.ChefResponseDto;
import dev.awd.tab5abackend.exception.ChefAlreadyExistException;
import dev.awd.tab5abackend.model.Chef;

import java.util.List;

public interface ChefService {
    List<ChefResponseDto> findAll();

    ChefResponseDto findById(Long id);

    ChefResponseDto save(ChefRequestDto chefRequest) throws ChefAlreadyExistException;

    Chef findEntityById(Long id);
}
