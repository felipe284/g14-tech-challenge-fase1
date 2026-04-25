package com.fiap_g14.foodlink.api.service;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import com.fiap_g14.foodlink.api.dto.LoginRequestDTO;
import com.fiap_g14.foodlink.api.dto.LoginResponseDTO;
import com.fiap_g14.foodlink.api.enums.UserTypeEnum;
import com.fiap_g14.foodlink.api.exception.BusinessException;
import com.fiap_g14.foodlink.api.repository.UserRepository;
import com.fiap_g14.foodlink.api.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService - Testes na camada Serviço")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private AuthService authService;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve validar login com sucesso")
    void testValidateLoginSuccess() {
        UserEntity user = UserEntity.builder()
                .id(userId)
                .login("joao.silva")
                .senha("senhaAtual123")
                .tipoUsuario(UserTypeEnum.CLIENTE)
                .build();

        when(userRepository.findByLogin("joao.silva")).thenReturn(Optional.of(user));
        doNothing().when(userValidator).validateCredentials(user, "plain");

        LoginRequestDTO request = new LoginRequestDTO("joao.silva", "plain");
        LoginResponseDTO response = authService.validateLogin(request);

        assertNotNull(response);
        assertEquals(userId, response.getUserId());
        assertEquals("joao.silva", response.getLogin());
        assertEquals(UserTypeEnum.CLIENTE, response.getTipoUsuario());
    }

    @Test
    @DisplayName("Deve lançar 401 quando usuário não existir")
    void testValidateLoginUserNotFound() {
        when(userRepository.findByLogin("unknown")).thenReturn(Optional.empty());

        LoginRequestDTO request = new LoginRequestDTO("unknown", "any");

        BusinessException ex = assertThrows(BusinessException.class, () -> authService.validateLogin(request));
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
        assertEquals("Login ou senha inválidos", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar 401 quando senha estiver incorreta")
    void testValidateLoginWrongPassword() {
        UserEntity user = UserEntity.builder()
                .id(userId)
                .login("joao.silva")
                .senha("senhaAtual123")
                .build();

        when(userRepository.findByLogin("joao.silva")).thenReturn(Optional.of(user));
        doThrow(new BusinessException("Login ou senha inválidos", org.springframework.http.HttpStatus.UNAUTHORIZED))
                .when(userValidator).validateCredentials(user, "wrong");

        LoginRequestDTO request = new LoginRequestDTO("joao.silva", "wrong");

        BusinessException ex = assertThrows(BusinessException.class, () -> authService.validateLogin(request));
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
        assertEquals("Login ou senha inválidos", ex.getMessage());
    }
}

