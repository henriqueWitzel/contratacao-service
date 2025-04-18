package br.com.challenge.contratacao.domain.entity;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class ContratacaoTest {

    @Test
    void deveCriarContratacaoComDadosValidos() {
        UUID id = UUID.randomUUID();
        LocalDateTime agora = LocalDateTime.now();

        Contratacao contratacao = new Contratacao(
                id,
                "12345678900",
                "Seguro Automotivo",
                new BigDecimal("100.00"),
                "CRIADA",
                agora,
                "BMW M3 Competition",
                2021,
                "Sudeste"
        );

        assertEquals(id, contratacao.getId());
        assertEquals("12345678900", contratacao.getCpfCliente());
        assertEquals("Seguro Automotivo", contratacao.getProduto());
        assertEquals(new BigDecimal("100.00"), contratacao.getValor());
        assertEquals("CRIADA", contratacao.getStatus());
        assertEquals(agora, contratacao.getDataCriacao());
        assertEquals("BMW M3 Competition", contratacao.getModeloVeiculo());
        assertEquals(2021, contratacao.getAnoFabricacao());
        assertEquals("Sudeste", contratacao.getRegiaoContratacao());
    }
}