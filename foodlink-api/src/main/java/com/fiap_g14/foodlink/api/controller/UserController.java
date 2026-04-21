package com.fiap_g14.foodlink.api.controller;

import com.fiap_g14.foodlink.api.dto.CreateUserRequestDTO;
import com.fiap_g14.foodlink.api.dto.UserResponseDTO;
import com.fiap_g14.foodlink.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "Criar um novo usuário")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "409", description = "Usuário já possui um cadastro"),
            @ApiResponse(responseCode = "400", description = "Erro de Regra de Negócio"),
            @ApiResponse(responseCode = "429", description = "Dados de entrada inválidos")
    })
    @PostMapping()
    public UserResponseDTO createUser(@Valid @RequestBody CreateUserRequestDTO userRequestDTO){
        return service.createUser(userRequestDTO);
    }
}
