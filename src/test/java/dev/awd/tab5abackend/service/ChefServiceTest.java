package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.request.ChefRequestDto;
import dev.awd.tab5abackend.dto.response.ChefResponseDto;
import dev.awd.tab5abackend.exception.ChefAlreadyExistException;
import dev.awd.tab5abackend.exception.ChefNotFoundException;
import dev.awd.tab5abackend.mapper.ChefMapper;
import dev.awd.tab5abackend.model.Chef;
import dev.awd.tab5abackend.repository.ChefRepository;
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
class ChefServiceTest {

    @InjectMocks
    private ChefServiceImpl chefService;
    @Mock
    private ChefRepository chefRepository;
    @Mock
    private ChefMapper chefMapper;
    @Mock
    UploadService uploadService;

    @Test
    void ChefService_FindAll_ReturnsAllChefs() {
        List<Chef> chefList = List.of(
                new Chef(1L, "ali", "top chef", "images/ali.png", Instant.now()),
                new Chef(1L, "mohamed", "top cooker", "images/mohamed.png", Instant.now()),
                new Chef(2L, "ola", "ola bio", "images/ola.png", Instant.now())
        );
        when(chefRepository.findAll()).thenReturn(chefList);
        List<ChefResponseDto> allChefs = chefService.findAll();
        assertNotNull(allChefs);
        assertFalse(allChefs.isEmpty());
        assertEquals(chefList.size(), 3);
    }

    @Test
    void ChefService_FindById_ReturnsChefResponseDto() {
        Chef chef = new Chef(1L, "ali", "top chef", "images/ali.png", Instant.now());
        ChefResponseDto expectedDto = new ChefResponseDto(1L, "ali", "top chef", "images/ali.png", Instant.now());

        when(chefRepository.findById(anyLong())).thenReturn(Optional.of(chef));
        when(chefMapper.chefToChefResponseDto(chef)).thenReturn(expectedDto);
        ChefResponseDto responseDto = chefService.findById(1L);
        assertNotNull(responseDto);
        assertEquals(responseDto.getBio(), chef.getBio());
    }

    @Test
    void ChefService_FindByNonExistingId_ThrowsException() {
        when(chefRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ChefNotFoundException.class, () -> chefService.findById(1L));
    }

    @SneakyThrows
    @Test
    void ChefService_CreateNewChefWithValidInfo_ChefCreated() {
        Chef chef = new Chef(1L, "ali", "top chef", "images/ali.png", Instant.now());
        MockMultipartFile image = new MockMultipartFile("image", "test.png", "image/png", "fake image".getBytes());
        ChefRequestDto chefRequest = new ChefRequestDto("chef", "chef-bio", image);
        ChefResponseDto expectedDto = new ChefResponseDto(1L, "ali", "top chef", "images/ali.png", Instant.now());

        when(chefMapper.chefRequestDtoToChef(any(ChefRequestDto.class))).thenReturn(chef);
        when(chefRepository.save(any(Chef.class))).thenReturn(chef);
        when(chefMapper.chefToChefResponseDto(any(Chef.class))).thenReturn(expectedDto);

        ChefResponseDto responseDto = chefService.save(chefRequest);

        assertNotNull(responseDto);
        assertEquals(responseDto.getId(), 1L);
    }

    @SneakyThrows
    @Test
    void ChefService_CreateNewChefWithExistingName_ThrowsException() {
        MockMultipartFile image = new MockMultipartFile("image", "test.png", "image/png", "fake image".getBytes());
        ChefRequestDto chefRequest = new ChefRequestDto("chef", "chef-bio", image);

        when(chefRepository.existsByName(anyString())).thenReturn(true);

        assertThrows(ChefAlreadyExistException.class, () -> chefService.save(chefRequest));

    }

}