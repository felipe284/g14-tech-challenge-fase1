package com.fiap_g14.foodlink.api.repository;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<UserEntity> nomeLike(String nome) {
        return (root, query, criteriaBuilder) -> {
            if (nome == null || nome.isBlank()) return null;
            String pattern = "%" + nome.toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), pattern);
        };
    }
}

