package com.fiap_g14.foodlink.api.controller;


import com.fiap_g14.foodlink.api.dto.CreateUserRequestDTO;
import com.fiap_g14.foodlink.api.dto.ChangePasswordRequestDTO;
import com.fiap_g14.foodlink.api.dto.UserResponseDTO;
import com.fiap_g14.foodlink.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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


    @Operation(summary = "Criar um novo usuário")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "409", description = "Usuário já possui um cadastro"),
            @ApiResponse(responseCode = "400", description = "Erro de Regra de Negócio"),
            @ApiResponse(responseCode = "429", description = "Dados de entrada inválidos")
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUser(@Valid @RequestBody CreateUserRequestDTO userRequestDTO) {
        return service.createUser(userRequestDTO);
    }

    @Operation(summary = "Excluir usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Erro de Regra de Negócio")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID id) {
        service.deleteUser(id);
    }

    @Operation(summary = "Alterar a senha do usuário")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "204", description = "Senha do usuário alterado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "400", description = "Erro de Regra de Negócio"),
        @ApiResponse(responseCode = "422", description = "Dados de entrada inválidos")
    })
    @PatchMapping("/change-password/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Validated @RequestBody ChangePasswordRequestDTO request, @PathVariable UUID id) {
        service.changePassword(id, request);

    }
}
