package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.request.LoginRequestDto;
import dev.awd.tab5abackend.dto.request.RegisterRequestDto;
import dev.awd.tab5abackend.dto.response.AuthenticationResponseDto;
import dev.awd.tab5abackend.exception.UserAlreadyExistException;

public interface AuthenticationService {

    AuthenticationResponseDto register(RegisterRequestDto request) throws UserAlreadyExistException;

    AuthenticationResponseDto login(LoginRequestDto request);
}