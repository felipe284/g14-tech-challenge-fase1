package com.fiap_g14.foodlink.api.validator.pagination;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaginationValidator {

    private final List<PaginationRule> rules;

    public void validate(Integer page, Integer size) {
        rules.forEach(rule -> rule.validate(page, size));
    }
}
