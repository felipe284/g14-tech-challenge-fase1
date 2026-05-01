package com.fiap_g14.foodlink.api.service;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import com.fiap_g14.foodlink.api.dto.ChangePasswordRequestDTO;
import com.fiap_g14.foodlink.api.dto.CreateUserRequestDTO;
import com.fiap_g14.foodlink.api.dto.UserResponseDTO;
import com.fiap_g14.foodlink.api.exception.BusinessException;
import com.fiap_g14.foodlink.api.exception.DataAlreadyExistsException;
import com.fiap_g14.foodlink.api.helper.MockHelper;
import com.fiap_g14.foodlink.api.mapper.PageResponseMapper;
import com.fiap_g14.foodlink.api.repository.UserRepository;
import com.fiap_g14.foodlink.api.security.PasswordHasher;
import com.fiap_g14.foodlink.api.validator.UserUniquenessValidator;
import com.fiap_g14.foodlink.api.validator.UserValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

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
    private UserUniquenessValidator userUniquenessValidator;

    @Spy
    private PageResponseMapper pageResponseMapper;

    @Mock
    private PasswordHasher passwordHasher;

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
                .senha("senhaAtualCriptografada")
                .build();
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("senhaAtual123", "novaSenha456");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userValidator).validateChangePassword(user, request);
        when(passwordHasher.encode("novaSenha456")).thenReturn("novaSenhaCriptografada");

        assertDoesNotThrow(() -> userService.changePassword(userId, request));

        assertEquals("novaSenhaCriptografada", user.getSenha());
        verify(userRepository, times(1)).findById(userId);
        verify(userValidator, times(1)).validateChangePassword(user, request);
        verify(passwordHasher, times(1)).encode("novaSenha456");
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

        doNothing().when(userUniquenessValidator).validateForCreate(userRequestDTO.getEmail(), userRequestDTO.getLogin());
        when(passwordHasher.encode(userRequestDTO.getSenha())).thenReturn("senhaCriptografada");
        when(userRepository.save(any())).thenReturn(MockHelper.getMockUserEntity());

        UserResponseDTO responseDTO = userService.createUser(userRequestDTO);

        verify(userUniquenessValidator, times(1)).validateForCreate(userRequestDTO.getEmail(), userRequestDTO.getLogin());
        verify(passwordHasher, times(1)).encode(userRequestDTO.getSenha());
        assertEquals("Maria de Souza", responseDTO.getNome());
        assertEquals("maria@teste.com.br", responseDTO.getEmail());
        assertEquals("Mariadesouza", responseDTO.getLogin());
        assertNotNull(responseDTO.getId());
    }

    @Test
    @DisplayName("Deve retornar usuários paginados com sucesso")
    void testGetAllUsersWithPagination() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome"));
        UserEntity user1 = MockHelper.getMockUserEntity();
        UserEntity user2 = UserEntity.builder()
                .id(java.util.UUID.randomUUID())
                .nome("Carlos")
                .email("carlos@teste.com")
                .login("carlos")
                .senha("senha")
                .tipoUsuario(user1.getTipoUsuario())
                .build();

        var page = new PageImpl<>(java.util.List.of(user1, user2), pageable, 2);

        when(userRepository.findAll(ArgumentMatchers.<Specification<UserEntity>>any(), ArgumentMatchers.any(Pageable.class))).thenReturn(page);
        doNothing().when(userValidator).validatePagination(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());

        var response = userService.getUsers(0, 10, null);

        assertEquals(0, response.getPage());
        assertEquals(10, response.getSize());
        assertEquals(2, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(2, response.getContent().size());
    }

    @Test
    @DisplayName("Deve filtrar usuários por nome com paginação")
    void testGetAllUsersWithNameFilter() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome"));
        UserEntity user1 = MockHelper.getMockUserEntity();
        var page = new PageImpl<>(java.util.List.of(user1), pageable, 1);

        when(userRepository.findAll(ArgumentMatchers.<Specification<UserEntity>>any(), ArgumentMatchers.any(Pageable.class))).thenReturn(page);

        var response = userService.getUsers(0 , 10 , "Maria");

        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getContent().size());
        assertEquals("Maria de Souza", response.getContent().get(0).getNome());
    }

    @Test
    @DisplayName("Deve lançar exception quando o usuário já possui email cadastrado")
    void testThrowExceptionWhenEmailAlreadyExists() {
        CreateUserRequestDTO userRequestDTO = MockHelper.getCreateUserRequestDTO();

        doThrow(new DataAlreadyExistsException("Já existe um usuário cadastrado com o email: maria@teste.com.br ou login: Mariadesouza"))
                .when(userUniquenessValidator).validateForCreate(userRequestDTO.getEmail(), userRequestDTO.getLogin());

        try{
            userService.createUser(userRequestDTO);
            fail();
        } catch (DataAlreadyExistsException e){
            assertEquals("Já existe um usuário cadastrado com o email: maria@teste.com.br ou login: Mariadesouza", e.getMessage());
        }

        verify(userRepository, never()).save(any());
        verify(passwordHasher, never()).encode(any());
    }
}
