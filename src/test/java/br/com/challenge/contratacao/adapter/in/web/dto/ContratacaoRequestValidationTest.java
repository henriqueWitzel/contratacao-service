package br.com.challenge.contratacao.adapter.in.web.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
class ContratacaoRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void deveDetectarCpfEmBranco() {
        ContratacaoRequest dto = new ContratacaoRequest("", "Seguro Vida", BigDecimal.TEN);
        Set<ConstraintViolation<ContratacaoRequest>> violations = validator.validate(dto);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("cpfCliente"));
    }

    @Test
    void deveDetectarProdutoNulo() {
        ContratacaoRequest dto = new ContratacaoRequest("12345678900", null, BigDecimal.TEN);
        Set<ConstraintViolation<ContratacaoRequest>> violations = validator.validate(dto);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("produto"));
    }

    @Test
    void deveDetectarValorNegativo() {
        ContratacaoRequest dto = new ContratacaoRequest("12345678900", "Seguro Vida", new BigDecimal("-10"));
        Set<ConstraintViolation<ContratacaoRequest>> violations = validator.validate(dto);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("valor"));
    }
}
