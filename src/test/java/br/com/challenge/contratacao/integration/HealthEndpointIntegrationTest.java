package br.com.challenge.contratacao.integration;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HealthEndpointIntegrationTest {

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void deveRetornarStatusUpParaHealthCheck() {
        // Monta a URL para o endpoint de health
        String url = "http://localhost:" + port + "/actuator/health";

        // Faz a requisição GET
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Verifica se a resposta foi bem-sucedida (2xx)
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        // Verifica se o corpo contém o status "UP"
        assertThat(response.getBody()).contains("\"status\":\"UP\"");
    }
}
