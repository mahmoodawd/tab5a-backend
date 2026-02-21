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
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final DataMaskingUtil dataMaskingUtil;

    @SneakyThrows
    @Override
    public AuthenticationResponseDto register(RegisterRequestDto request) {
        String maskedEmail = dataMaskingUtil.maskEmail(request.getEmail());
        String maskedPhone = dataMaskingUtil.maskPhone(request.getMobile());

        log.info("User registration initiated for email: {}", maskedEmail);

        if (userRepository.existsByEmailOrMobile(request.getEmail(), request.getMobile())) {
            log.warn("Registration failed - duplicate email or phone: {}, {}", maskedEmail, maskedPhone);
            throw new UserAlreadyExistException();
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully. userId={}, email={}",
                savedUser.getId(), maskedEmail);

        String jwtToken = jwtService.generateToken(savedUser);
        log.debug("JWT token generated for userId={}", savedUser.getId());

        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponseDto login(LoginRequestDto request) {
        String maskedEmail = dataMaskingUtil.maskEmail(request.getEmail());

        log.info("Login attempt for email: {}", maskedEmail);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("Authenticated user not found in database: {}", maskedEmail);
                    return new UserNotFoundException(maskedEmail);
                });

        log.info("Login successful. userId={}, email={}", user.getId(), maskedEmail);

        String jwtToken = jwtService.generateToken(user);
        log.debug("JWT token generated for userId={}", user.getId());

        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .build();
    }

}