package com.fiap_g14.foodlink.api.validator;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import com.fiap_g14.foodlink.api.dto.ChangePasswordRequestDTO;
import com.fiap_g14.foodlink.api.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserValidator - Testes de Validação")
class UserValidatorTest {

    private UserValidator userValidator;

    private UserEntity user;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        userValidator = new UserValidator(passwordEncoder);

        user = UserEntity.builder()
                .nome("João Silva")
                .email("joao@email.com")
                .login("joaosilva")
                .senha( passwordEncoder.encode("senhaAtual123"))
                .build();
    }

    @Test
    @DisplayName("Deve validar sem erros quando a senha atual está correta")
    void testValidateChangePassword_success() {
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("senhaAtual123", "novaSenha456");

        assertDoesNotThrow(() -> userValidator.validateChangePassword(user, request));
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando a senha atual está incorreta")
    void testValidateChangePassword_wrongCurrentPassword() {
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("senhaErrada", "novaSenha456");

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userValidator.validateChangePassword(user, request)
        );

        assertEquals("Senha atual incorreta", exception.getMessage());
    }
}

