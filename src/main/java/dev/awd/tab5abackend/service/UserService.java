package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.response.UsersListResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface UserService extends UserDetailsService {

    String uploadAvatar(String userEmail, MultipartFile avatar);

    UsersListResponseDto findAll();

    void grantAdminPermissions(UUID userId);

}