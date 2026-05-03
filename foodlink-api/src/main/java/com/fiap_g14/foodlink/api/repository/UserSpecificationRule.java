package com.fiap_g14.foodlink.api.repository;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import org.springframework.data.jpa.domain.Specification;

public interface UserSpecificationRule {

    Specification<UserEntity> build(UserFilter filter);
}
