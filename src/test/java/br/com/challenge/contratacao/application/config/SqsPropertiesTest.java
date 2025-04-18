package br.com.challenge.contratacao.application.config;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Embora utilize @SpringBootTest para carregar o contexto,
 * este teste é leve e visa garantir a configuração correta das propriedades SQS.
 * A anotação @Tag("unit") é mantida para que este teste seja considerado na cobertura unitária.
 */
@Tag("unit")
@SpringBootTest
@ActiveProfiles("test")
class SqsPropertiesTest {

    @Autowired
    private SqsProperties properties;

    @Test
    void deveCarregarConfiguracaoDeNovaVendaUrl() {
        assertThat(properties.getNovaVendaUrl())
                .isNotBlank()
                .contains("nova-venda-criada");
    }
}