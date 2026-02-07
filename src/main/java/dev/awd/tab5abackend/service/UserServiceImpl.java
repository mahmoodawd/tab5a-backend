package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.ImageType;
import dev.awd.tab5abackend.dto.response.UserResponseDto;
import dev.awd.tab5abackend.dto.response.UsersListResponseDto;
import dev.awd.tab5abackend.exception.UserNotFoundException;
import dev.awd.tab5abackend.mapper.UserMapper;
import dev.awd.tab5abackend.mapper.resolver.ImageUrlResolver;
import dev.awd.tab5abackend.model.Role;
import dev.awd.tab5abackend.model.User;
import dev.awd.tab5abackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UploadService uploadService;
    private final UserMapper userMapper;
    private final ImageUrlResolver imageUrlResolver;


    @Override
    public String uploadAvatar(String userEmail, MultipartFile avatar) {
        User targetUser = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException(userEmail));
        String avatarUrl = uploadService.uploadImage(avatar, ImageType.USER);
        targetUser.setAvatar(avatarUrl);
        userRepository.save(targetUser);
        return imageUrlResolver.toUrl(avatarUrl);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UsersListResponseDto findAll() {
        List<UserResponseDto> usersList = userRepository.findAll().stream().map(userMapper::userToUserResponseDto).toList();
        Date lastUpdated = usersList.stream()
                .filter(Objects::nonNull)
                .map(UserResponseDto::getUpdatedAt)
                .filter(Objects::nonNull)
                .max(Date::compareTo)
                .orElse(null);
        return new UsersListResponseDto(usersList.size(), lastUpdated, usersList);
    }

    @Override
    public void grantAdminPermissions(UUID userId) {
        User targetUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId.toString()));
        targetUser.setRole(Role.ADMIN);
        userRepository.save(targetUser);
    }
}