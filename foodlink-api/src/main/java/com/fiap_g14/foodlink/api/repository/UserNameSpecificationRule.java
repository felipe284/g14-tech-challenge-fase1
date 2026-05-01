package com.fiap_g14.foodlink.api.repository;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserNameSpecificationRule implements UserSpecificationRule {

    @Override
    public Specification<UserEntity> build(UserFilter filter) {
        return (root, query, criteriaBuilder) -> {
            if (filter.name() == null || filter.name().isBlank()) {
                return null;
            }

            String pattern = "%" + filter.name().toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), pattern);
        };
    }
}
