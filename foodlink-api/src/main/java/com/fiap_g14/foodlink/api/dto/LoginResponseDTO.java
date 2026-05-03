package com.fiap_g14.foodlink.api.dto;

import com.fiap_g14.foodlink.api.enums.UserTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private UUID userId;
    private String login;
    private UserTypeEnum tipoUsuario;
}

