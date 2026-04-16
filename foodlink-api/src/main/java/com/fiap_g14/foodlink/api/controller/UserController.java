package com.fiap_g14.foodlink.api.controller;

import com.fiap_g14.foodlink.api.dto.UpdateUserRequestDTO;
import com.fiap_g14.foodlink.api.dto.UserResponseDTO;
import com.fiap_g14.foodlink.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Usuários")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @Operation(summary = "Listar todos os usuários")
    @GetMapping()
    public List<UserResponseDTO> getAllUsers() {
        return service.getAllUsers();
    }

    //Endpoint para atualizar um usuário
    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
        @PathVariable UUID id,
        @RequestBody UpdateUserRequestDTO request) {

    UserResponseDTO updatedUser = service.updateUser(id, request);
    return ResponseEntity.ok(updatedUser);
}
}
