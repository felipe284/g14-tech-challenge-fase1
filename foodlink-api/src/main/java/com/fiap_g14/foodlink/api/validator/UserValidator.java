package com.fiap_g14.foodlink.api.validator;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import com.fiap_g14.foodlink.api.dto.ChangePasswordRequestDTO;
import com.fiap_g14.foodlink.api.exception.BusinessException;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final BCryptPasswordEncoder passwordEncoder;

    public void validateChangePassword(UserEntity user, ChangePasswordRequestDTO request) {
        if (!passwordEncoder.matches(request.getSenhaAtual(), user.getSenha())) {
            throw new BusinessException("Senha atual incorreta");
        }
    }

    public void validateCredentials(UserEntity user, String rawPassword) {
        if (!passwordEncoder.matches(rawPassword, user.getSenha())) {
            throw new BusinessException("Login ou senha inválidos", HttpStatus.UNAUTHORIZED);
        }
    }

    public void validatePagination(Integer pageActual, Integer size) {
        if (size < 1) {
            throw new BusinessException("Parâmetros size devem ser maior que zero");
        }

        if (pageActual < 0) {
            throw new BusinessException("Parâmetros page devem ser maior ou igual a zero");
        }
    }
}
