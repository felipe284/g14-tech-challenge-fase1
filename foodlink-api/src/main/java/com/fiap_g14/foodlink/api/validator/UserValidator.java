package com.fiap_g14.foodlink.api.validator;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import com.fiap_g14.foodlink.api.dto.ChangePasswordRequestDTO;
import com.fiap_g14.foodlink.api.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    public void validateChangePassword(UserEntity user, ChangePasswordRequestDTO request) {
        if (!user.getSenha().equals(request.getSenhaAtual())) {
            throw new BusinessException("Senha atual incorreta");
        }
    }
}
