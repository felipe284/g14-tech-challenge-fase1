package com.fiap_g14.foodlink.api.repository;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserSpecificationBuilder {

    private final List<UserSpecificationRule> rules;

    public Specification<UserEntity> build(UserFilter filter) {
        Specification<UserEntity> emptySpecification = (root, query, criteriaBuilder) -> null;

        return rules.stream()
                .map(rule -> rule.build(filter))
                .reduce(emptySpecification, Specification::and);
    }
}
