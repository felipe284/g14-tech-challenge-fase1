package com.fiap_g14.foodlink.api.helper;

import com.fiap_g14.foodlink.api.domain.Address;
import com.fiap_g14.foodlink.api.domain.UserEntity;
import com.fiap_g14.foodlink.api.dto.AddressDTO;
import com.fiap_g14.foodlink.api.dto.CreateUserRequestDTO;
import com.fiap_g14.foodlink.api.dto.UserResponseDTO;
import com.fiap_g14.foodlink.api.enums.UserTypeEnum;

import java.time.OffsetDateTime;
import java.util.UUID;

public class MockHelper {
    public static UserEntity getMockUserEntity(){
        return UserEntity.builder()
                .id(UUID.fromString("acde070d-8c4c-4f0d-9d8a-162843c10333"))
                .nome("Maria de Souza")
                .login("Mariadesouza")
                .email("maria@teste.com.br")
                .senha("senhateste")
                .dataUltimaAlteracao(OffsetDateTime.now())
                .tipoUsuario(UserTypeEnum.CLIENTE)
                .endereco(Address.builder()
                        .logradouro("Rua teste")
                        .numero("100")
                        .complemento("Casa")
                        .bairro("Bairro do norte")
                        .cidade("São Paulo")
                        .cep("13800000")
                        .uf("SP").build()).build();
    }

    public static CreateUserRequestDTO getCreateUserRequestDTO() {
        return CreateUserRequestDTO.builder()
                .nome("Maria de Souza")
                .login("Mariadesouza")
                .email("maria@teste.com.br")
                .senha("senhateste")
                .tipoUsuario(UserTypeEnum.CLIENTE)
                .endereco(AddressDTO.builder()
                        .logradouro("Rua teste")
                        .numero("100")
                        .complemento("Casa")
                        .bairro("Bairro do norte")
                        .cidade("São Paulo")
                        .cep("13800000")
                        .uf("SP").build()).build();
    }

    public static UserResponseDTO getMockUserResponseDTO() {
        return UserResponseDTO.builder()
                .id(UUID.fromString("acde070d-8c4c-4f0d-9d8a-162843c10333"))
                .nome("Maria de Souza")
                .login("Mariadesouza")
                .email("maria@teste.com.br")
                .endereco(AddressDTO.builder()
                        .logradouro("Rua teste")
                        .numero("100")
                        .complemento("Casa")
                        .bairro("Bairro do norte")
                        .cidade("São Paulo")
                        .cep("13800000")
                        .uf("SP").build()).build();
    }
}
