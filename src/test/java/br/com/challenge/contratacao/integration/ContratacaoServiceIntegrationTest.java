package br.com.challenge.contratacao.integration;

import br.com.challenge.contratacao.adapter.in.web.dto.ContratacaoRequest;
import br.com.challenge.contratacao.adapter.out.persistence.ContratacaoRepositoryImpl;
import br.com.challenge.contratacao.domain.entity.Contratacao;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ContratacaoServiceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ContratacaoRepositoryImpl repository;

    @Autowired
    private AmazonSQS amazonSQS;

    @Autowired
    private ObjectMapper objectMapper;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String QUEUE_URL = "http://localhost:4566/000000000000/nova-venda-criada";

    @Test
    @Order(1)
    void deveCriarContratacaoComSucesso() {
        // Arrange
        ContratacaoRequest request = new ContratacaoRequest(
                "12345678900",
                "Seguro de Vida IntegrationTest",
                new BigDecimal("99.99")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ContratacaoRequest> httpEntity = new HttpEntity<>(request, headers);
        URI uri = URI.create("http://localhost:" + port + "/contratacoes");

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(uri, httpEntity, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Extrai ID retornado
        String location = response.getHeaders().getLocation().toString();
        String id = location.substring(location.lastIndexOf("/") + 1);
        UUID uuid = UUID.fromString(id);

        // Valida persistência
        Contratacao contratacao = repository.buscarPorId(uuid).orElse(null);
        assertThat(contratacao).isNotNull();
        assertThat(contratacao.getProduto()).isEqualTo("Seguro de Vida IntegrationTest");

        // Verifica mensagem na fila
        var sqsRequest = new ReceiveMessageRequest()
                .withQueueUrl(QUEUE_URL)
                .withMaxNumberOfMessages(10);

        List<com.amazonaws.services.sqs.model.Message> mensagens = amazonSQS.receiveMessage(sqsRequest).getMessages();
        assertThat(mensagens).anyMatch(m -> m.getBody().contains("Seguro de Vida IntegrationTest"));

        // Cleanup: remove do banco e limpa fila
        repository.deletar(uuid);
        mensagens.forEach(mensagem -> amazonSQS.deleteMessage(QUEUE_URL, mensagem.getReceiptHandle()));
    }

    @Test
    @Order(2)
    void deveFalharComCpfEmBranco() {
        // Arrange
        ContratacaoRequest request = new ContratacaoRequest(
                "", // CPF inválido
                "Seguro Auto",
                new BigDecimal("200.00")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ContratacaoRequest> httpEntity = new HttpEntity<>(request, headers);
        URI uri = URI.create("http://localhost:" + port + "/contratacoes");

        // Act & Assert
        try {
            restTemplate.postForEntity(uri, httpEntity, String.class);
            fail("Era esperado erro 400 para CPF em branco.");
        } catch (org.springframework.web.client.HttpClientErrorException.BadRequest ex) {
            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

            // Apenas para log visual no console (removível depois)
            System.out.println(">> Resposta de erro: \n" + ex.getResponseBodyAsString());
        }
    }


}
