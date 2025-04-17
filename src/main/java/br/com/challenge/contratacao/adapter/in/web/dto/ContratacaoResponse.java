package br.com.challenge.contratacao.adapter.in.web.dto;

import br.com.challenge.contratacao.domain.entity.Contratacao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados retornados de uma contratação existente")
public record ContratacaoResponse(
        @Schema(description = "Identificador da contratação", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
        UUID id,

        @Schema(description = "CPF do cliente", example = "12345678900")
        String cpfCliente,

        @Schema(description = "Produto contratado", example = "Seguro Automotivo")
        String produto,

        @Schema(description = "Valor da contratação", example = "150.00")
        BigDecimal valor,

        @Schema(description = "Status atual da contratação", example = "CRIADA")
        String status,

        @Schema(description = "Data de criação do registro", example = "2025-04-16T22:00:00")
        LocalDateTime dataCriacao
) {
    public ContratacaoResponse(Contratacao contratacao) {
        this(
                contratacao.getId(),
                contratacao.getCpfCliente(),
                contratacao.getProduto(),
                contratacao.getValor(),
                contratacao.getStatus(),
                contratacao.getDataCriacao()
        );
    }
}
