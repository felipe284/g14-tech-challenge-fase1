package com.fiap_g14.foodlink.api.service;

import com.fiap_g14.foodlink.api.dto.UserResponseDTO;
import com.fiap_g14.foodlink.api.mapper.UserMapper;
import com.fiap_g14.foodlink.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toDTO).toList();
    }
}
