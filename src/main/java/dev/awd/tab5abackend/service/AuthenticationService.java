package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.request.LoginRequestDto;
import dev.awd.tab5abackend.dto.request.RegisterRequestDto;
import dev.awd.tab5abackend.dto.response.AuthenticationResponseDto;

public interface AuthenticationService {

    AuthenticationResponseDto register(RegisterRequestDto request);

    AuthenticationResponseDto login(LoginRequestDto request);
}