package com.fiap_g14.foodlink.api.dto;

import com.fiap_g14.foodlink.api.enums.UserTypeEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequestDTO {
    @NotBlank(message = "O campo nome é obrigatório")
    private String nome;
    @NotBlank(message = "O campo email é obrigatório")
    @Email(message = "O campo email deve ser um endereço de email válido")
    private String email;
    @NotBlank(message = "O campo login é obrigatório")
    private String login;
    @NotBlank(message = "O campo senha é obrigatório")
    private String senha;
    private UserTypeEnum tipoUsuario;
    @NotNull(message = "O campo endereco é obrigatório")
    private AddressDTO endereco;
}
