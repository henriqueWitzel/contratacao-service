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

    private ContratacaoRequest criarRequestValido() {
        return new ContratacaoRequest(
                "12345678900",
                "Seguro Automotivo",
                BigDecimal.TEN,
                "Hyundai HB20",
                2021,
                "Sudeste"
        );
    }

    @Test
    void deveDetectarCpfEmBranco() {
        var dto = criarRequestValido();
        dto = new ContratacaoRequest(
                "",
                dto.produto(),
                dto.valor(),
                dto.modeloVeiculo(),
                dto.anoFabricacao(),
                dto.regiaoContratacao()
        );

        Set<ConstraintViolation<ContratacaoRequest>> violations = validator.validate(dto);
        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("cpfCliente"));
    }

    @Test
    void deveDetectarProdutoNulo() {
        var dto = criarRequestValido();
        dto = new ContratacaoRequest(
                dto.cpfCliente(),
                null,
                dto.valor(),
                dto.modeloVeiculo(),
                dto.anoFabricacao(),
                dto.regiaoContratacao()
        );

        Set<ConstraintViolation<ContratacaoRequest>> violations = validator.validate(dto);
        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("produto"));
    }

    @Test
    void deveDetectarValorNegativo() {
        var dto = criarRequestValido();
        dto = new ContratacaoRequest(
                dto.cpfCliente(),
                dto.produto(),
                new BigDecimal("-10"),
                dto.modeloVeiculo(),
                dto.anoFabricacao(),
                dto.regiaoContratacao()
        );

        Set<ConstraintViolation<ContratacaoRequest>> violations = validator.validate(dto);
        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("valor"));
    }

    @Test
    void deveDetectarModeloVeiculoEmBranco() {
        var dto = criarRequestValido();
        dto = new ContratacaoRequest(
                dto.cpfCliente(),
                dto.produto(),
                dto.valor(),
                "",
                dto.anoFabricacao(),
                dto.regiaoContratacao()
        );

        Set<ConstraintViolation<ContratacaoRequest>> violations = validator.validate(dto);
        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("modeloVeiculo"));
    }

    @Test
    void deveDetectarAnoFabricacaoInvalido() {
        var dto = criarRequestValido();
        dto = new ContratacaoRequest(
                dto.cpfCliente(),
                dto.produto(),
                dto.valor(),
                dto.modeloVeiculo(),
                1800,
                dto.regiaoContratacao()
        );

        Set<ConstraintViolation<ContratacaoRequest>> violations = validator.validate(dto);
        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("anoFabricacao"));
    }

    @Test
    void deveDetectarRegiaoEmBranco() {
        var dto = criarRequestValido();
        dto = new ContratacaoRequest(
                dto.cpfCliente(),
                dto.produto(),
                dto.valor(),
                dto.modeloVeiculo(),
                dto.anoFabricacao(),
                ""
        );

        Set<ConstraintViolation<ContratacaoRequest>> violations = validator.validate(dto);
        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("regiaoContratacao"));
    }
}