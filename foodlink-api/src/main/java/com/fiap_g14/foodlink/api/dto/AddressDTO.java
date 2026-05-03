package com.fiap_g14.foodlink.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    @NotBlank(message = "O campo logradouro é obrigatório")
    private String logradouro;
    @NotBlank(message = "O numero é obrigatório")
    private String numero;
    @NotBlank(message = "o complemento é obrigatório")
    private String complemento;
    @NotBlank(message = "o complemento é obrigatório")
    private String bairro;
    @NotBlank(message = "o bairro é obrigatório")
    private String cidade;
    @NotBlank(message = "o uf é obrigatório")
    private String uf;
    @NotBlank(message = "o cep é obrigatório")
    private String cep;
}
