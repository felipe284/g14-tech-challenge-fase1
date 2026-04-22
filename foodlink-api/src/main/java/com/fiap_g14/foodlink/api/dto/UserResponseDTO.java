package com.fiap_g14.foodlink.api.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class UserResponseDTO {

    private UUID id;
    private String nome;
    private String email;
    private String login;
    private OffsetDateTime dataUltimaAlteracao;
    private AddressDTO endereco;
}
