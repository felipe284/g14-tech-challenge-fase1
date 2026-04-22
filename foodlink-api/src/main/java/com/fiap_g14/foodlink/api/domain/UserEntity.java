package com.fiap_g14.foodlink.api.domain;

import com.fiap_g14.foodlink.api.enums.UserTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Email(message = "Formato de e-mail inválido")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private UserTypeEnum tipoUsuario;

    @UpdateTimestamp
    @Column(name = "data_ultima_alteracao", nullable = false)
    private OffsetDateTime dataUltimaAlteracao;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "logradouro", column = @Column(name = "address_logradouro")),
            @AttributeOverride(name = "numero", column = @Column(name = "address_numero")),
            @AttributeOverride(name = "complemento", column = @Column(name = "address_complemento")),
            @AttributeOverride(name = "bairro", column = @Column(name = "address_bairro")),
            @AttributeOverride(name = "cidade", column = @Column(name = "address_cidade")),
            @AttributeOverride(name = "uf", column = @Column(name = "address_uf")),
            @AttributeOverride(name = "cep", column = @Column(name = "address_cep"))
    })
    private Address endereco;

    @PrePersist
    @PreUpdate
    public void atualizarData() {
        this.dataUltimaAlteracao = OffsetDateTime.now();
    }
}