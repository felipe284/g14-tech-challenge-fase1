package com.fiap_g14.foodlink.api.validator;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import com.fiap_g14.foodlink.api.dto.ChangePasswordRequestDTO;
import com.fiap_g14.foodlink.api.exception.BusinessException;
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
}
