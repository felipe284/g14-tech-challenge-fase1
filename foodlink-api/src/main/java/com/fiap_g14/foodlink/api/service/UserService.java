package com.fiap_g14.foodlink.api.service;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import com.fiap_g14.foodlink.api.dto.CreateUserRequestDTO;
import com.fiap_g14.foodlink.api.dto.UserResponseDTO;
import com.fiap_g14.foodlink.api.exceptions.DataAlreadyExistsException;
import com.fiap_g14.foodlink.api.mapper.UserMapper;
import com.fiap_g14.foodlink.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
}
