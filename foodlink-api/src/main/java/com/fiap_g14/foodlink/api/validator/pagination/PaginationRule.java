package com.fiap_g14.foodlink.api.validator.pagination;

public interface PaginationRule {

    void validate(Integer page, Integer size);
}
