package com.fiap_g14.foodlink.api.service;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import com.fiap_g14.foodlink.api.dto.UpdateUserRequestDTO;
import com.fiap_g14.foodlink.api.dto.UserResponseDTO;
import com.fiap_g14.foodlink.api.mapper.UserMapper;
import com.fiap_g14.foodlink.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toDTO).toList();
    }

    //Update user method
    public UserResponseDTO updateUser(UUID id, UpdateUserRequestDTO request) {

    UserEntity user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (request.getName() != null) {
            user.setName(request.getName());
            
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        return UserMapper.toDTO(userRepository.save(user));
    
    }
}
