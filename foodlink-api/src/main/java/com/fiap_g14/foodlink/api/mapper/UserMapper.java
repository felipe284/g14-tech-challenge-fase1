package com.fiap_g14.foodlink.api.mapper;

import com.fiap_g14.foodlink.api.domain.Address;
import com.fiap_g14.foodlink.api.domain.UserEntity;
import com.fiap_g14.foodlink.api.dto.AddressDTO;
import com.fiap_g14.foodlink.api.dto.CreateUserRequestDTO;
import com.fiap_g14.foodlink.api.dto.UpdateUserRequestDTO;
import com.fiap_g14.foodlink.api.dto.UserResponseDTO;

public class UserMapper {

    public static UserResponseDTO toDTO(UserEntity user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .nome(user.getNome())
                .email(user.getEmail())
                .login(user.getLogin())
                .dataUltimaAlteracao(user.getDataUltimaAlteracao())
                .endereco(toAddressDTO(user.getEndereco()))
                .tipoUsuario(user.getTipoUsuario())
                .build();
    }

    public static UserEntity toEntity(CreateUserRequestDTO userDTO, String passwordHash) {
        return UserEntity.builder()
                .nome(userDTO.getNome())
                .email(userDTO.getEmail())
                .login(userDTO.getLogin())
                .senha(passwordHash)
                .tipoUsuario(userDTO.getTipoUsuario())
                .endereco(toAddressEntity(userDTO.getEndereco()))
                .build();
    }

    public static Address toAddressEntity(AddressDTO addressDTO) {
        if (addressDTO == null) return null;

        return Address.builder()
                .logradouro(addressDTO.getLogradouro())
                .numero(addressDTO.getNumero())
                .complemento(addressDTO.getComplemento())
                .bairro(addressDTO.getBairro())
                .cidade(addressDTO.getCidade())
                .uf(addressDTO.getUf())
                .cep(addressDTO.getCep())
                .build();
    }

    private static AddressDTO toAddressDTO(Address address) {
        if (address == null) return null;

        return AddressDTO.builder()
                .logradouro(address.getLogradouro())
                .numero(address.getNumero())
                .complemento(address.getComplemento())
                .bairro(address.getBairro())
                .cidade(address.getCidade())
                .uf(address.getUf())
                .cep(address.getCep())
                .build();
    }

    

    public static void updateEntity(UpdateUserRequestDTO dto, UserEntity entity) {
            entity.setNome(dto.getNome());
            entity.setEmail(dto.getEmail());
            entity.setLogin(dto.getLogin());
            entity.setTipoUsuario(dto.getTipoUsuario());

            updateAddress(dto.getEndereco(), entity.getEndereco());

        }

    public static void updateAddress(AddressDTO dto, Address entity) {

            entity.setLogradouro(dto.getLogradouro());
            entity.setNumero(dto.getNumero());
            entity.setComplemento(dto.getComplemento());
            entity.setBairro(dto.getBairro());
            entity.setCidade(dto.getCidade());
            entity.setUf(dto.getUf());
            entity.setCep(dto.getCep());
    }

}
