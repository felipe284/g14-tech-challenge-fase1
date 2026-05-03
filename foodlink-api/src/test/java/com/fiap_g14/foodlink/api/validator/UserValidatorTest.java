package com.fiap_g14.foodlink.api.validator;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import com.fiap_g14.foodlink.api.dto.ChangePasswordRequestDTO;
import com.fiap_g14.foodlink.api.exception.BusinessException;
import com.fiap_g14.foodlink.api.security.PasswordHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserValidator - Testes de Validação")
class UserValidatorTest {

    @InjectMocks
    private UserValidator userValidator;

    private UserEntity user;

    @Mock
    private PasswordHasher passwordHasher;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
                .nome("João Silva")
                .email("joao@email.com")
                .login("joaosilva")
                .senha("senhaCriptografada")
                .build();
    }

    @Test
    @DisplayName("Deve validar sem erros quando a senha atual está correta")
    void testValidateChangePassword_success() {
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("senhaAtual123", "novaSenha456");
        when(passwordHasher.matches("senhaAtual123", "senhaCriptografada")).thenReturn(true);

        assertDoesNotThrow(() -> userValidator.validateChangePassword(user, request));
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando a senha atual está incorreta")
    void testValidateChangePassword_wrongCurrentPassword() {
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("senhaErrada", "novaSenha456");
        when(passwordHasher.matches("senhaErrada", "senhaCriptografada")).thenReturn(false);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userValidator.validateChangePassword(user, request)
        );

        assertEquals("Senha atual incorreta", exception.getMessage());
    }

    @Test
    @DisplayName("Deve validar credenciais com sucesso quando a senha estiver correta")
    void testValidateCredentials_success() {
        when(passwordHasher.matches("senhaAtual123", "senhaCriptografada")).thenReturn(true);

        assertDoesNotThrow(() -> userValidator.validateCredentials(user, "senhaAtual123"));
    }

    @Test
    @DisplayName("Deve lançar 401 quando senha estiver incorreta ao validar credenciais")
    void testValidateCredentials_wrongPassword() {
        when(passwordHasher.matches("senhaErrada", "senhaCriptografada")).thenReturn(false);

        BusinessException ex = assertThrows(BusinessException.class, () -> userValidator.validateCredentials(user, "senhaErrada"));
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
        assertEquals("Login ou senha inválidos", ex.getMessage());
    }
}
