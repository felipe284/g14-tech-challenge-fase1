package com.fiap_g14.foodlink.api.validator;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import com.fiap_g14.foodlink.api.exception.DataAlreadyExistsException;
import com.fiap_g14.foodlink.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUniquenessValidator {

    private final UserRepository userRepository;

    public void validateForCreate(String email, String login) {
        userRepository.findByEmailAndLogin(email, login)
                .ifPresent(user -> {
                    throw new DataAlreadyExistsException("Já existe um usuário cadastrado com o email: " + email + " ou login: " + login);
                });
    }

    public void validateForUpdate(UserEntity currentUser, String email, String login) {
        if (!email.equalsIgnoreCase(currentUser.getEmail()) && userRepository.existsByEmail(email)) {
            throw new DataAlreadyExistsException("Já existe o email cadastrado: " + email);
        }

        if (!login.equalsIgnoreCase(currentUser.getLogin()) && userRepository.existsByLogin(login)) {
            throw new DataAlreadyExistsException("Já existe o login cadastrado: " + login);
        }
    }
}
