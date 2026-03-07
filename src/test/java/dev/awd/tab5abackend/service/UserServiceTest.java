package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.ImageType;
import dev.awd.tab5abackend.dto.response.UsersListResponseDto;
import dev.awd.tab5abackend.exception.UserNotFoundException;
import dev.awd.tab5abackend.mapper.UserMapper;
import dev.awd.tab5abackend.mapper.resolver.ImageUrlResolver;
import dev.awd.tab5abackend.model.Role;
import dev.awd.tab5abackend.model.User;
import dev.awd.tab5abackend.repository.UserRepository;
import dev.awd.tab5abackend.util.DataMaskingUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    UploadService uploadService;
    @Spy
    UserMapper userMapper;
    @Mock
    ImageUrlResolver imageUrlResolver;

    @Spy
    DataMaskingUtil dataMaskingUtil;


    @Test
    void UserService_UpdateExistingUserAvatar_ReturnsValidUrl() {
        String userEmail = "test.user@email.com";

        MockMultipartFile avatar = new MockMultipartFile(
                "profile", "test.png", "image/png", "fake image".getBytes()
        );

        User user = User.builder()
                .name("Test User")
                .mobile("123456789")
                .role(Role.USER)
                .build();
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(uploadService.uploadImage(avatar, ImageType.USER)).thenReturn("samplePath");
        when(imageUrlResolver.toUrl("samplePath")).thenReturn("sampleUrl");
        String responseUrl = userService.uploadAvatar(userEmail, avatar);

        assertEquals(responseUrl, "sampleUrl");

    }

    @Test
    void UserService_UpdateNonExistingUserAvatar_ThrowsException() {
        String userEmail = "test2.user@email.com";

        MockMultipartFile avatar = new MockMultipartFile(
                "profile", "test.png", "image/png", "fake image".getBytes()
        );

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.uploadAvatar(userEmail, avatar));
    }

    @Test
    void UserService_FindAll_ReturnsAllUsers() {
        User user = new User();
        when(userRepository.findAll()).thenReturn(List.of(user));
        UsersListResponseDto response = userService.findAll();
        assertNotNull(response);
        assertNotNull(response.getUsers());
        assertEquals(response.getCount(), 1);
        assertTrue(response.getUsers().contains(userMapper.userToUserResponseDto(user)));
    }

    @Test
    void UserService_FindByEmail_ReturnsUser() {
        User user = User.builder().name("test-user").email("test-user@email.com").build();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        UserDetails response = userService.loadUserByUsername(user.getEmail());
        assertNotNull(response);
        assertNotNull(response.getUsername());
        assertEquals(response.getUsername(), "test-user@email.com");
    }

    @Test
    void UserService_FindByNonExistingEmail_ThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(""));
    }

    @Test
    void UserService_GrantAdminAccess_UpdatesUser() {
        User user = User.builder().id(UUID.randomUUID()).name("test-user")
                .role(Role.USER).email("test-user@email.com").build();
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.grantAdminPermissions(user.getId());
        assertEquals(user.getRole(), Role.ADMIN);
        verify(userRepository).save(any(User.class));

    }

    @Test
    void UserService_GrantAdminForAdmin_DoNothing() {
        User user = User.builder().id(UUID.randomUUID()).name("test-user").email("test-user@email.com").role(Role.ADMIN).build();
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        userService.grantAdminPermissions(user.getId());

        verify(userRepository, never()).save(any(User.class));
        assertEquals(user.getRole(), Role.ADMIN);
    }


}