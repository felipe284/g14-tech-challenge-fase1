package com.fiap_g14.foodlink.api.dto;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressResponseDTO {
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
}
