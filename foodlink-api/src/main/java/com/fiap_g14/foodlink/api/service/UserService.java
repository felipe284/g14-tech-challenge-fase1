package com.fiap_g14.foodlink.api.service;

import com.fiap_g14.foodlink.api.domain.Address;
import com.fiap_g14.foodlink.api.domain.UserEntity;
import com.fiap_g14.foodlink.api.dto.*;
import com.fiap_g14.foodlink.api.exception.DataAlreadyExistsException;
import com.fiap_g14.foodlink.api.mapper.UserMapper;
import com.fiap_g14.foodlink.api.repository.UserRepository;
import com.fiap_g14.foodlink.api.security.PasswordHasher;
import com.fiap_g14.foodlink.api.validator.UserValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.fiap_g14.foodlink.api.repository.UserSpecification;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final PasswordHasher passwordEncoder;

    public PageResponseDTO getUsers(Integer pageActual, Integer size, String name) {

        userValidator.validatePagination(pageActual, size);

        Pageable pageable = PageRequest.of(pageActual, size, Sort.by("nome"));

        Specification<UserEntity> spec = UserSpecification.nomeLike(name);
        Page<UserEntity> page = userRepository.findAll(spec, pageable);

        List<UserResponseDTO> content = page.getContent().stream().map(UserMapper::toDTO).collect(Collectors.toList());

        return PageResponseDTO.builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    public UserResponseDTO createUser(CreateUserRequestDTO userRequestDTO) {
        Optional<UserEntity> existingUserOpt = userRepository.findByEmailAndLogin(userRequestDTO.getEmail(), userRequestDTO.getLogin());
        if(existingUserOpt.isPresent()){
            throw new DataAlreadyExistsException("Já existe um usuário cadastrado com o email: " + userRequestDTO.getEmail() + " ou login: " + userRequestDTO.getLogin());
        }
        UserEntity entity = userRepository.save(UserMapper.toEntity(userRequestDTO, passwordEncoder.encode(userRequestDTO.getSenha())));
        return UserMapper.toDTO(entity);
    }

    public UserResponseDTO getUserById(UUID id) {
        return userRepository.findById(id)
                .map(UserMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
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

        user.setSenha(passwordEncoder.encode(changePasswordRequestDTO.getNovaSenha()));
        userRepository.save(user);
    }


    public UserResponseDTO updateUser(UUID id, UpdateUserRequestDTO dto) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if(!dto.getEmail().equalsIgnoreCase(user.getEmail())){
           if (userRepository.existsByEmail(dto.getEmail())){
               throw new DataAlreadyExistsException("Já existe o email cadastrado: " + dto.getEmail() );
           }
        }

        if(!dto.getLogin().equalsIgnoreCase(user.getLogin())){
            if (userRepository.existsByLogin(dto.getLogin())){
                throw new DataAlreadyExistsException("Já existe o login cadastrado: " + dto.getLogin() );
            }
        }

        UserMapper.updateEntity(dto, user);

        return UserMapper.toDTO(user);
    }
}
