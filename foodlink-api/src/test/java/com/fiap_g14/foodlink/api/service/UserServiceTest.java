package com.fiap_g14.foodlink.api.service;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import com.fiap_g14.foodlink.api.dto.ChangePasswordRequestDTO;
import com.fiap_g14.foodlink.api.dto.CreateUserRequestDTO;
import com.fiap_g14.foodlink.api.dto.UserResponseDTO;
import com.fiap_g14.foodlink.api.exception.BusinessException;
import com.fiap_g14.foodlink.api.exception.DataAlreadyExistsException;
import com.fiap_g14.foodlink.api.helper.MockHelper;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

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
                .senha(passwordEncoder.encode("senhaAtual123"))
                .build();
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("senhaAtual123", "novaSenha456");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userValidator).validateChangePassword(user, request);

        assertDoesNotThrow(() -> userService.changePassword(userId, request));

        assertEquals( passwordEncoder.encode("novaSenha456"), user.getSenha());
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

    @Test
    @DisplayName("Deve criar um usuário com sucesso")
    void testCreateUserSuccessfully() {
        CreateUserRequestDTO userRequestDTO = MockHelper.getCreateUserRequestDTO();

        when(userRepository.findByEmailAndLogin(userRequestDTO.getEmail(), userRequestDTO.getLogin())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userRequestDTO.getSenha())).thenReturn("senhaCriptografada");
        when(userRepository.save(any())).thenReturn(MockHelper.getMockUserEntity());

        UserResponseDTO responseDTO = userService.createUser(userRequestDTO);

        assertEquals("Maria de Souza", responseDTO.getNome());
        assertEquals("maria@teste.com.br", responseDTO.getEmail());
        assertEquals("Mariadesouza", responseDTO.getLogin());
        assertNotNull(responseDTO.getId());
    }

    @Test
    @DisplayName("Deve lançar exception quando o usuário já possui email cadastrado")
    void testThrowExceptionWhenEmailAlreadyExists() {
        CreateUserRequestDTO userRequestDTO = MockHelper.getCreateUserRequestDTO();

        when(userRepository.findByEmailAndLogin(userRequestDTO.getEmail(), userRequestDTO.getLogin()))
                .thenReturn(Optional.of(MockHelper.getMockUserEntity()));

        try{
            userService.createUser(userRequestDTO);
            fail();
        } catch (DataAlreadyExistsException e){
            assertEquals("Já existe um usuário cadastrado com o email: maria@teste.com.br ou login: Mariadesouza", e.getMessage());
        }

    }
}
