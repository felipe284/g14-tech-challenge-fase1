package com.fiap_g14.foodlink.api.controller;

import com.fiap_g14.foodlink.api.config.GlobalExceptionHandler;
import com.fiap_g14.foodlink.api.service.UserService;
import com.fiap_g14.foodlink.api.dto.UserResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController - Testes na Camada de Controle")
class UserControllerTest {

    private MockMvc mockMvc;

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
    @DisplayName("Deve retornar 200 e o usuário quando ID existir")
    void testGetUserByIdSuccess() throws Exception {
        UUID userId = UUID.randomUUID();
        var dto = UserResponseDTO.builder()
                .id(userId)
                .nome("João Silva")
                .email("joao.silva@email.com")
                .login("joao.silva")
                .dataUltimaAlteracao(null)
                .build();

        when(userService.getUserById(userId)).thenReturn(dto);

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao.silva@email.com"));

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    @DisplayName("Deve retornar 404 quando buscar usuário inexistente")
    void testGetUserByIdNotFound() throws Exception {
        UUID userId = UUID.randomUUID();
        when(userService.getUserById(userId))
                .thenThrow(new EntityNotFoundException("Usuário não encontrado"));

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Usuário não encontrado"));

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    @DisplayName("Deve retornar 400 quando ID não é um UUID válido (GET)")
    void testGetUserByIdInvalidUUID() throws Exception {
        mockMvc.perform(get("/users/{id}", "invalid-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("ID deve ser um UUID válido"));
    }
}
