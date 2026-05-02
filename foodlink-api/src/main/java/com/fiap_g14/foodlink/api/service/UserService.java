package com.fiap_g14.foodlink.api.service;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import com.fiap_g14.foodlink.api.dto.*;
import com.fiap_g14.foodlink.api.mapper.PageResponseMapper;
import com.fiap_g14.foodlink.api.mapper.UserMapper;
import com.fiap_g14.foodlink.api.repository.UserRepository;
import com.fiap_g14.foodlink.api.repository.UserFilter;
import com.fiap_g14.foodlink.api.repository.UserSpecificationBuilder;
import com.fiap_g14.foodlink.api.security.PasswordHasher;
import com.fiap_g14.foodlink.api.validator.pagination.PaginationValidator;
import com.fiap_g14.foodlink.api.validator.UserUniquenessValidator;
import com.fiap_g14.foodlink.api.validator.UserValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserUniquenessValidator userUniquenessValidator;
    private final PaginationValidator paginationValidator;
    private final UserSpecificationBuilder userSpecificationBuilder;
    private final PageResponseMapper pageResponseMapper;
    private final PasswordHasher passwordHasher;

    public PageResponseDTO getUsers(Integer currentPage, Integer size, String name) {

        paginationValidator.validate(currentPage, size);

        Pageable pageable = PageRequest.of(currentPage, size, Sort.by("nome"));

        Specification<UserEntity> spec = userSpecificationBuilder.build(new UserFilter(name));
        Page<UserEntity> page = userRepository.findAll(spec, pageable);

        return pageResponseMapper.toUserPageResponse(page);
    }

    public UserResponseDTO createUser(CreateUserRequestDTO userRequestDTO) {
        userUniquenessValidator.validateForCreate(userRequestDTO.getEmail(), userRequestDTO.getLogin());

        UserEntity entity = userRepository.save(UserMapper.toEntity(userRequestDTO, passwordHasher.encode(userRequestDTO.getSenha())));
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

        user.setSenha(passwordHasher.encode(changePasswordRequestDTO.getNovaSenha()));
        userRepository.save(user);
    }


    public UserResponseDTO updateUser(UUID id, UpdateUserRequestDTO dto) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        userUniquenessValidator.validateForUpdate(user, dto.getEmail(), dto.getLogin());

        UserMapper.updateEntity(dto, user);

        userRepository.save(user);
        return UserMapper.toDTO(user);
    }
}
