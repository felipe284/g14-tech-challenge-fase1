package com.fiap_g14.foodlink.api.service;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import com.fiap_g14.foodlink.api.dto.LoginRequestDTO;
import com.fiap_g14.foodlink.api.dto.LoginResponseDTO;
import com.fiap_g14.foodlink.api.exception.BusinessException;
import com.fiap_g14.foodlink.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.fiap_g14.foodlink.api.validator.UserValidator;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserValidator userValidator;

    public LoginResponseDTO validateLogin(LoginRequestDTO request) {
        UserEntity user = userRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new BusinessException("Login ou senha inválidos", HttpStatus.UNAUTHORIZED));

        userValidator.validateCredentials(user, request.getSenha());

        return LoginResponseDTO.builder()
                .userId(user.getId())
                .login(user.getLogin())
                .tipoUsuario(user.getTipoUsuario())
                .build();
    }
}

