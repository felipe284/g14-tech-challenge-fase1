package com.fiap_g14.foodlink.api.repository;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("UserSpecificationBuilder - Testes de Specifications")
class UserSpecificationBuilderTest {

    @Test
    @DisplayName("Deve montar specification executando as regras cadastradas")
    void testBuildSpecificationWithRules() {
        UserFilter filter = new UserFilter("Maria");
        UserSpecificationRule rule = mock(UserSpecificationRule.class);
        when(rule.build(filter)).thenReturn((root, query, criteriaBuilder) -> null);

        UserSpecificationBuilder builder = new UserSpecificationBuilder(List.of(rule));

        Specification<UserEntity> specification = builder.build(filter);

        assertNotNull(specification);
        verify(rule, times(1)).build(filter);
    }
}
