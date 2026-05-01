package com.fiap_g14.foodlink.api.validator.pagination;

import com.fiap_g14.foodlink.api.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class PageSizePaginationRule implements PaginationRule {

    @Override
    public void validate(Integer page, Integer size) {
        if (size < 1) {
            throw new BusinessException("Parâmetros size devem ser maior que zero");
        }
    }
}
