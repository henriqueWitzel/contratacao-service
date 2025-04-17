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
        Contratacao contratacao = new Contratacao(UUID.randomUUID(), "12345678900", "Seguro Automotivo", new BigDecimal("100.00"), "CRIADA", LocalDateTime.now());
        assertEquals("12345678900", contratacao.getCpfCliente());
        assertEquals("Seguro Automotivo", contratacao.getProduto());
        assertEquals(new BigDecimal("100.00"), contratacao.getValor());
    }
}
