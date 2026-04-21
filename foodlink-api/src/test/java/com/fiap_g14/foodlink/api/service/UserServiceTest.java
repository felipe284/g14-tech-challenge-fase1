package com.fiap_g14.foodlink.api.service;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import com.fiap_g14.foodlink.api.dto.ChangePasswordRequestDTO;
import com.fiap_g14.foodlink.api.exception.BusinessException;
import com.fiap_g14.foodlink.api.repository.UserRepository;
import com.fiap_g14.foodlink.api.validator.UserValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService - Testes na camada Serviço")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserService userService;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve deletar usuário com sucesso")
    void testDeleteUserSuccess() {
        when(userRepository.existsById(userId)).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser(userId));

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não existe")
    void testDeleteUserNotFound() {
        when(userRepository.existsById(userId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.deleteUser(userId)
        );

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve alterar a senha do usuário com sucesso")
    void testChangePasswordSuccess() {
        UserEntity user = UserEntity.builder()
                .id(userId)
                .nome("João Silva")
                .email("joao@email.com")
                .login("joaosilva")
                .senha("senhaAtual123")
                .build();
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("senhaAtual123", "novaSenha456");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userValidator).validateChangePassword(user, request);

        assertDoesNotThrow(() -> userService.changePassword(userId, request));

        assertEquals("novaSenha456", user.getSenha());
        verify(userRepository, times(1)).findById(userId);
        verify(userValidator, times(1)).validateChangePassword(user, request);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando usuário não encontrado ao alterar senha")
    void testChangePasswordUserNotFound() {
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("senhaAtual123", "novaSenha456");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.changePassword(userId, request)
        );

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userValidator, never()).validateChangePassword(any(), any());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando a senha atual está incorreta")
    void testChangePasswordWrongCurrentPassword() {
        UserEntity user = UserEntity.builder()
                .id(userId)
                .nome("João Silva")
                .email("joao@email.com")
                .login("joaosilva")
                .senha("senhaAtual123")
                .build();
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("senhaErrada", "novaSenha456");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doThrow(new BusinessException("Senha atual incorreta"))
                .when(userValidator).validateChangePassword(user, request);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userService.changePassword(userId, request)
        );

        assertEquals("Senha atual incorreta", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
}
