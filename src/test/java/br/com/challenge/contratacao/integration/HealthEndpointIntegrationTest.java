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
    private static final String HEALTH_STATUS_EXPECTED = "\"status\":\"UP\"";

    @Test
    void deveRetornarStatusUpParaHealthCheck() {
        String url = "http://localhost:" + port + "/actuator/health";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful())
                .as("Deveria retornar status HTTP 2xx")
                .isTrue();

        assertThat(response.getBody())
                .as("Corpo da resposta deveria conter status UP")
                .contains(HEALTH_STATUS_EXPECTED);
    }
}