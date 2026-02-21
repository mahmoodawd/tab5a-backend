package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.request.LoginRequestDto;
import dev.awd.tab5abackend.dto.request.RegisterRequestDto;
import dev.awd.tab5abackend.dto.response.AuthenticationResponseDto;
import dev.awd.tab5abackend.exception.UserAlreadyExistException;
import dev.awd.tab5abackend.exception.UserNotFoundException;
import dev.awd.tab5abackend.model.Role;
import dev.awd.tab5abackend.model.User;
import dev.awd.tab5abackend.repository.UserRepository;
import dev.awd.tab5abackend.security.JwtService;
import dev.awd.tab5abackend.util.DataMaskingUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserRepository userRepository;
    @Mock
    JwtService jwtService;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    PasswordEncoder passwordEncoder;
    @Spy
    DataMaskingUtil dataMaskingUtil;


    @Test
    void authenticationService_Register_ReturnsValidToken() {
        RegisterRequestDto request = RegisterRequestDto.builder()
                .name("Test User")
                .mobile("123456789")
                .email("test@test.com")
                .password("password")
                .build();
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(jwtService.generateToken(any(User.class))).thenReturn("sampleToken");
        AuthenticationResponseDto response = authenticationService.register(request);

        assertEquals(response.getToken(), "sampleToken");


    }

    @Test
    void AuthenticationService_RegisterWithExistingCredentials_ThrowsException() {
        RegisterRequestDto request = RegisterRequestDto.builder()
                .name("Test User")
                .mobile("123456789")
                .email("test@test.com")
                .password("password")
                .build();
        when(userRepository.existsByEmailOrMobile(request.getEmail(), request.getMobile())).thenReturn(true);
        assertThrows(UserAlreadyExistException.class, () -> authenticationService.register(request));

    }

    @Test
    void AuthenticationService_LoginWithValidCredentials_ReturnsValidToken() {
        LoginRequestDto request = LoginRequestDto.builder()
                .email("test@test.com")
                .password("password")
                .build();
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(User.builder()
                .name("Test User")
                .mobile("123456789")
                .role(Role.USER)
                .build()));
        when(jwtService.generateToken(any(User.class))).thenReturn("sampleToken");
        AuthenticationResponseDto response = authenticationService.login(request);

        assertEquals(response.getToken(), "sampleToken");

    }

    @Test
    void AuthenticationService_LoginWithInvalidCredentials_ThrowsException() {
        LoginRequestDto request = LoginRequestDto.builder()
                .email("test@test.com")
                .password("password")
                .build();
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> authenticationService.login(request));

    }


}