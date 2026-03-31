package com.fiap_g14.foodlink.api.controller;

import com.fiap_g14.foodlink.api.config.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Operation(
            summary = "Gerar token JWT",
            description = "Autentica o usuário e retorna um access token JWT"
    )
    @ApiResponse(responseCode = "200", description = "Token gerado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    @PostMapping("/token")
    public TokenResponse token(@RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        String token = jwtService.generateToken(auth.getName(), auth.getAuthorities());
        return new TokenResponse(token);
    }

    public record LoginRequest(
            @NotBlank
            @Schema(description = "Login do usuário", example = "admin")
            String username,

            @NotBlank
            @Schema(description = "Senha do usuário", example = "123456")
            String password
    ) {}
    public record TokenResponse(
            @Schema(description = "JWT para autenticação Bearer", example = "eyJhbGciOiJIUzI1NiJ9...")
            String accessToken
    ) {}
}
