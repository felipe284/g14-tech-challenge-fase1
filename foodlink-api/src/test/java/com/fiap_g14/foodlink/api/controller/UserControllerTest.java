package com.fiap_g14.foodlink.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap_g14.foodlink.api.dto.ChangePasswordRequestDTO;
import com.fiap_g14.foodlink.api.exception.BusinessException;
import com.fiap_g14.foodlink.api.exception.GlobalExceptionHandler;
import com.fiap_g14.foodlink.api.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController - Testes na Camada de Controle")
class UserControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Deve retornar 204 No Content ao deletar usuário com sucesso")
    void testDeleteUserSuccess() throws Exception {
        UUID userId = UUID.randomUUID();
        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    @DisplayName("Deve retornar 404 quando usuário não existe")
    void testDeleteUserNotFound() throws Exception {
        UUID userId = UUID.randomUUID();
        doThrow(new EntityNotFoundException("Usuário não encontrado"))
                .when(userService).deleteUser(userId);

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Usuário não encontrado"));

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    @DisplayName("Deve retornar 400 quando ID não é um UUID válido")
    void testDeleteUserInvalidUUID() throws Exception {
        mockMvc.perform(delete("/users/{id}", "invalid-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("ID deve ser um UUID válido"));
    }

    @Test
    @DisplayName("Deve retornar 204 No Content ao alterar senha com sucesso")
    void testChangePasswordSuccess() throws Exception {
        UUID userId = UUID.randomUUID();
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("senhaAtual123", "novaSenha456");
        doNothing().when(userService).changePassword(userId, request);

        mockMvc.perform(patch("/users/change-password/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).changePassword(userId, request);
    }

    @Test
    @DisplayName("Deve retornar 404 quando usuário não encontrado ao alterar senha")
    void testChangePasswordUserNotFound() throws Exception {
        UUID userId = UUID.randomUUID();
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("senhaAtual123", "novaSenha456");
        doThrow(new EntityNotFoundException("Usuário não encontrado"))
                .when(userService).changePassword(userId, request);

        mockMvc.perform(patch("/users/change-password/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Usuário não encontrado"));

        verify(userService, times(1)).changePassword(userId, request);
    }

    @Test
    @DisplayName("Deve retornar 422 quando a senha atual está incorreta")
    void testChangePasswordWrongCurrentPassword() throws Exception {
        UUID userId = UUID.randomUUID();
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("senhaErrada", "novaSenha456");
        doThrow(new BusinessException("Senha atual incorreta"))
                .when(userService).changePassword(userId, request);

        mockMvc.perform(patch("/users/change-password/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.message").value("Senha atual incorreta"));

        verify(userService, times(1)).changePassword(userId, request);
    }

    @Test
    @DisplayName("Deve retornar 422 quando campos obrigatórios estão ausentes no body")
    void testChangePasswordMissingFields() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(patch("/users/change-password/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Deve retornar 422 quando nova senha é igual à senha atual")
    void testChangePasswordSamePasswords() throws Exception {
        UUID userId = UUID.randomUUID();
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("senhaAtual123", "senhaAtual123");

        mockMvc.perform(patch("/users/change-password/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }
}
