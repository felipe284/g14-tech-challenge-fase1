package com.fiap_g14.foodlink.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap_g14.foodlink.api.dto.LoginRequestDTO;
import com.fiap_g14.foodlink.api.dto.LoginResponseDTO;
import com.fiap_g14.foodlink.api.enums.UserTypeEnum;
import com.fiap_g14.foodlink.api.exception.BusinessException;
import com.fiap_g14.foodlink.api.exception.GlobalExceptionHandler;
import com.fiap_g14.foodlink.api.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController - Testes na Camada de Controle")
class AuthControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Deve retornar 200 quando login for válido")
    void testLoginSuccess() throws Exception {
        UUID id = UUID.randomUUID();
        LoginResponseDTO resp = LoginResponseDTO.builder()
                .userId(id)
                .login("user")
                .tipoUsuario(UserTypeEnum.CLIENTE)
                .build();

        when(authService.validateLogin(any())).thenReturn(resp);

        LoginRequestDTO request = new LoginRequestDTO("user", "pass");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(id.toString()))
                .andExpect(jsonPath("$.login").value("user"));
    }

    @Test
    @DisplayName("Deve retornar 401 quando credenciais forem inválidas")
    void testLoginUnauthorized() throws Exception {
        when(authService.validateLogin(any()))
                .thenThrow(new BusinessException("Login ou senha inválidos", HttpStatus.UNAUTHORIZED));

        LoginRequestDTO request = new LoginRequestDTO("user", "wrong");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Login ou senha inválidos"));
    }
}

