package br.com.challenge.contratacao.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "Dados para criar uma nova contratação")
public record ContratacaoRequest(

        @Schema(description = "CPF do cliente", example = "12345678900")
        @NotBlank String cpfCliente,

        @Schema(description = "Nome do produto contratado", example = "Seguro Automotivo")
        @NotBlank String produto,

        @Schema(description = "Valor da contratação", example = "150.00")
        @NotNull @DecimalMin("0.01") BigDecimal valor,

        @Schema(description = "Modelo do veículo", example = "Hyundai HB20")
        @NotBlank String modeloVeiculo,

        @Schema(description = "Ano de fabricação do veículo", example = "2021")
        @Min(1900) @Max(2100) int anoFabricacao,

        @Schema(description = "Região do país onde foi realizada a contratação", example = "Sudeste")
        @NotBlank String regiaoContratacao

) {}