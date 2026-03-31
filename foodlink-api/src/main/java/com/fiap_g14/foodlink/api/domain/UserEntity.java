package com.fiap_g14.foodlink.api.domain;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String senha;

    @Column(name = "data_ultima_alteracao", nullable = false)
    private OffsetDateTime dataUltimaAlteracao;

    @Embedded
    private Address address;

    @PrePersist
    @PreUpdate
    public void atualizarData() {
        this.dataUltimaAlteracao = OffsetDateTime.now();
    }
}