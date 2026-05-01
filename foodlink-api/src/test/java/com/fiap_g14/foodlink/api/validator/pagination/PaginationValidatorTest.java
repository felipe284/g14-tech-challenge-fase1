package com.fiap_g14.foodlink.api.validator.pagination;

import com.fiap_g14.foodlink.api.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("PaginationValidator - Testes de Validação")
class PaginationValidatorTest {

    private final PaginationValidator paginationValidator = new PaginationValidator(List.of(
            new PageNumberPaginationRule(),
            new PageSizePaginationRule()
    ));

    @Test
    @DisplayName("Deve validar paginação sem erros")
    void testValidateSuccess() {
        assertDoesNotThrow(() -> paginationValidator.validate(0, 10));
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando page for negativo")
    void testValidateNegativePage() {
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> paginationValidator.validate(-1, 10)
        );

        assertEquals("Parâmetros page devem ser maior ou igual a zero", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando size for menor que um")
    void testValidateInvalidSize() {
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> paginationValidator.validate(0, 0)
        );

        assertEquals("Parâmetros size devem ser maior que zero", exception.getMessage());
    }
}
