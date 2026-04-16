package com.fiap_g14.foodlink.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateUserRequestDTO {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;
}