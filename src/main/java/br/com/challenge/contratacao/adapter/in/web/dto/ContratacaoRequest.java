package br.com.challenge.contratacao.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Dados para criar uma nova contratação")
public record ContratacaoRequest(
        @Schema(description = "CPF do cliente", example = "12345678900")
        @NotBlank String cpfCliente,

        @Schema(description = "Nome do produto contratado", example = "Seguro de Vida")
        @NotBlank String produto,

        @Schema(description = "Valor da contratação", example = "150.00")
        @NotNull @DecimalMin("0.01") BigDecimal valor
) {}
