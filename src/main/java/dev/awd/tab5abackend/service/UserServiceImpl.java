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
import dev.awd.tab5abackend.util.DataMaskingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UploadService uploadService;
    private final UserMapper userMapper;
    private final ImageUrlResolver imageUrlResolver;
    private final DataMaskingUtil dataMaskingUtil;


    @Override
    public UsersListResponseDto findAll() {
        log.info("Fetching all users");

        List<UserResponseDto> usersList = userRepository.findAll().stream().map(userMapper::userToUserResponseDto).toList();

        log.debug("evaluating last update");
        Date lastUpdated = usersList.stream()
                .filter(Objects::nonNull)
                .map(UserResponseDto::getUpdatedAt)
                .filter(Objects::nonNull)
                .max(Date::compareTo)
                .orElse(null);

        log.info("Retrieved {} users", usersList.size());
        return new UsersListResponseDto(usersList.size(), lastUpdated, usersList);
    }

    @Override
    public String uploadAvatar(String userEmail, MultipartFile avatar) {
        log.info("Upload user avatar email={}", dataMaskingUtil.maskEmail(userEmail));

        log.debug("Fetching user from database");
        User targetUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", dataMaskingUtil.maskEmail(userEmail));
                    return new UserNotFoundException(userEmail);
                });

        log.debug("uploading user avatar");
        String avatarUrl = uploadService.uploadImage(avatar, ImageType.USER);
        targetUser.setAvatar(avatarUrl);

        log.debug("saving user to database");
        User savedUser = userRepository.save(targetUser);

        log.info("User avatar updated successfully id={}", savedUser.getId());
        return imageUrlResolver.toUrl(avatarUrl);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Fetching user with name");

        return userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.warn("User not found with name: {}", dataMaskingUtil.maskName(username));
                    return new UsernameNotFoundException("User not found");
                });
    }

    @Override
    public void grantAdminPermissions(UUID userId) {
        log.info("Granting admin permissions to user: {}", userId);

        User targetUser = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("User not found with id: {}", userId);
            return new UserNotFoundException(userId.toString());
        });

        targetUser.setRole(Role.ADMIN);

        log.debug("saving user to database");
        User savedUser = userRepository.save(targetUser);
        log.info("User granted admin permissions successfully id={}", savedUser.getId());
    }
}