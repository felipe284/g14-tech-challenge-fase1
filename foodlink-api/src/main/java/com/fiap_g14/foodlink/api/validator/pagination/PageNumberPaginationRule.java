package com.fiap_g14.foodlink.api.validator.pagination;

import com.fiap_g14.foodlink.api.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class PageNumberPaginationRule implements PaginationRule {

    @Override
    public void validate(Integer page, Integer size) {
        if (page < 0) {
            throw new BusinessException("Parâmetros page devem ser maior ou igual a zero");
        }
    }
}
