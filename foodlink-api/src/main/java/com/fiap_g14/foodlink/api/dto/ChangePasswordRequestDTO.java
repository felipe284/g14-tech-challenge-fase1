package com.fiap_g14.foodlink.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequestDTO {
    @Schema(description = "Senha atual do usuário", example = "senhaAtual123")
    @Size(min = 8, message = "A senha atual deve conter no mínimo 8 caracteres")
    @NotNull(message = "A senha atual é obrigatória")
    private String senhaAtual;

    @Schema(description = "Nova senha do usuário", example = "novaSenha123")
    @Size(min = 8, message = "A nova senha deve conter no mínimo 8 caracteres")
    @NotNull(message = "A nova senha é obrigatória")
    private String novaSenha;

    @AssertTrue(message = "A nova senha não pode ser igual à senha atual")
    @Schema( hidden = true )
    public boolean isSenhasDiferentes() {
        if (senhaAtual == null || novaSenha == null) return true;
        return !senhaAtual.equals(novaSenha);
    }
}
