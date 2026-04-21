package com.fiap_g14.foodlink.api.service;

import com.fiap_g14.foodlink.api.domain.UserEntity;

import com.fiap_g14.foodlink.api.dto.CreateUserRequestDTO;

import com.fiap_g14.foodlink.api.dto.ChangePasswordRequestDTO;

import com.fiap_g14.foodlink.api.dto.UserResponseDTO;
import com.fiap_g14.foodlink.api.exceptions.DataAlreadyExistsException;
import com.fiap_g14.foodlink.api.mapper.UserMapper;
import com.fiap_g14.foodlink.api.repository.UserRepository;
import com.fiap_g14.foodlink.api.validator.UserValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    private final BCryptPasswordEncoder passwordEncoder;

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toDTO).toList();
    }


    public UserResponseDTO createUser(CreateUserRequestDTO userRequestDTO) {
        Optional<UserEntity> existingUserOpt = userRepository.findByEmail(userRequestDTO.getEmail(), userRequestDTO.getLogin());
        if(existingUserOpt.isPresent()){
            throw new DataAlreadyExistsException("Já existe um usuário cadastrado com o email: " + userRequestDTO.getLogin());
        }

        UserEntity entity = userRepository.save(UserMapper.toEntity(userRequestDTO, passwordEncoder.encode(userRequestDTO.getSenha())));

        return UserMapper.toDTO(entity);
    }

    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        userRepository.deleteById(id);
    }

    public void changePassword(UUID id, ChangePasswordRequestDTO changePasswordRequestDTO) {

        UserEntity user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        userValidator.validateChangePassword(user, changePasswordRequestDTO);

        user.setSenha(changePasswordRequestDTO.getNovaSenha());
        userRepository.save(user);
    }
}
