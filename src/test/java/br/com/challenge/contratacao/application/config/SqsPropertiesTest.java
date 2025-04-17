package br.com.challenge.contratacao.application.config;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
@SpringBootTest
@ActiveProfiles("test") // lê do application-test.yml
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
