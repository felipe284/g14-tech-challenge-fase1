package com.fiap_g14.foodlink.api.mapper;

import com.fiap_g14.foodlink.api.domain.Address;
import com.fiap_g14.foodlink.api.domain.UserEntity;
import com.fiap_g14.foodlink.api.dto.AddressResponseDTO;
import com.fiap_g14.foodlink.api.dto.UserResponseDTO;

import java.util.UUID;

public class UserMapper {

    public static UserResponseDTO toDTO(UserEntity user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .nome(user.getNome())
                .email(user.getEmail())
                .login(user.getLogin())
                .dataUltimaAlteracao(user.getDataUltimaAlteracao())
                .address(toAddressDTO(user.getAddress()))
                .build();
    }

    private static AddressResponseDTO toAddressDTO(Address address) {
        if (address == null) return null;

        return AddressResponseDTO.builder()
                .logradouro(address.getLogradouro())
                .numero(address.getNumero())
                .complemento(address.getComplemento())
                .bairro(address.getBairro())
                .cidade(address.getCidade())
                .uf(address.getUf())
                .cep(address.getCep())
                .build();
    }
}
