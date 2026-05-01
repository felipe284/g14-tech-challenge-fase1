package com.fiap_g14.foodlink.api.validator;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import com.fiap_g14.foodlink.api.exception.DataAlreadyExistsException;
import com.fiap_g14.foodlink.api.helper.MockHelper;
import com.fiap_g14.foodlink.api.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserUniquenessValidator - Testes de Unicidade")
class UserUniquenessValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserUniquenessValidator userUniquenessValidator;

    @Test
    @DisplayName("Deve permitir criação quando email e login não existem")
    void shouldAllowCreateWhenEmailAndLoginDoNotExist() {
        when(userRepository.findByEmailAndLogin("novo@email.com", "novoLogin")).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userUniquenessValidator.validateForCreate("novo@email.com", "novoLogin"));
    }

    @Test
    @DisplayName("Deve bloquear criação quando email ou login já existem")
    void shouldBlockCreateWhenEmailOrLoginExists() {
        when(userRepository.findByEmailAndLogin("maria@teste.com.br", "Mariadesouza"))
                .thenReturn(Optional.of(MockHelper.getMockUserEntity()));

        DataAlreadyExistsException exception = assertThrows(
                DataAlreadyExistsException.class,
                () -> userUniquenessValidator.validateForCreate("maria@teste.com.br", "Mariadesouza")
        );

        assertEquals("Já existe um usuário cadastrado com o email: maria@teste.com.br ou login: Mariadesouza", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve consultar duplicidade quando email e login não mudaram")
    void shouldSkipDuplicateChecksWhenEmailAndLoginDidNotChange() {
        UserEntity user = MockHelper.getMockUserEntity();

        assertDoesNotThrow(() -> userUniquenessValidator.validateForUpdate(user, user.getEmail(), user.getLogin()));

        verify(userRepository, never()).existsByEmail(user.getEmail());
        verify(userRepository, never()).existsByLogin(user.getLogin());
    }

    @Test
    @DisplayName("Deve bloquear atualização quando novo email já existe")
    void shouldBlockUpdateWhenNewEmailExists() {
        UserEntity user = MockHelper.getMockUserEntity();
        when(userRepository.existsByEmail("outro@email.com")).thenReturn(true);

        DataAlreadyExistsException exception = assertThrows(
                DataAlreadyExistsException.class,
                () -> userUniquenessValidator.validateForUpdate(user, "outro@email.com", user.getLogin())
        );

        assertEquals("Já existe o email cadastrado: outro@email.com", exception.getMessage());
    }
}
