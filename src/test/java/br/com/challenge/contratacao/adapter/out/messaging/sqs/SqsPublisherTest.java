package br.com.challenge.contratacao.adapter.out.messaging.sqs;

import br.com.challenge.contratacao.application.config.SqsProperties;
import br.com.challenge.contratacao.application.exception.PublicacaoEventoException;
import br.com.challenge.contratacao.domain.entity.Contratacao;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class SqsPublisherTest {

    private AmazonSQS amazonSQS;
    private SqsPublisher publisher;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Configura o mock do cliente SQS
        amazonSQS = mock(AmazonSQS.class);

        // Configura as propriedades da fila SQS
        SqsProperties properties = new SqsProperties();
        properties.setNovaVendaUrl("http://localhost:4566/000000000000/nova-venda-criada");

        // Configura o ObjectMapper com suporte a Java Time (necessário para serializar LocalDateTime)
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Instancia o SqsPublisher com dependências mockadas
        publisher = new SqsPublisher(amazonSQS, properties, objectMapper);
    }

    @Test
    void devePublicarMensagemComSucesso() {
        // Cria uma instância fictícia de Contratacao
        Contratacao contratacao = new Contratacao(
                UUID.randomUUID(),
                "12345678900",
                "Seguro Automotivo",
                new BigDecimal("150.00"),
                "CRIADA",
                LocalDateTime.now()
        );

        // Executa o método de publicação
        publisher.publicar(contratacao);

        // Verifica se o método sendMessage foi chamado com qualquer SendMessageRequest
        verify(amazonSQS, times(1)).sendMessage(any(SendMessageRequest.class));
    }

    @Test
    void deveLancarErroQuandoUrlNaoForConfigurada() {
        // Configura propriedades sem URL de fila
        SqsProperties semUrl = new SqsProperties();

        // Cria publisher sem URL válida
        SqsPublisher publisherComErro = new SqsPublisher(amazonSQS, semUrl, objectMapper);

        // Cria contratação de teste
        Contratacao contratacao = new Contratacao(
                UUID.randomUUID(),
                "00000000000",
                "Teste",
                BigDecimal.ONE,
                null,
                null
        );

        // Verifica que uma IllegalArgumentException é lançada devido à URL nula
        assertThrows(IllegalArgumentException.class, () -> publisherComErro.publicar(contratacao));
    }

    @Test
    void deveLancarPublicacaoEventoExceptionQuandoFalharSerializacao() throws JsonProcessingException {
        // Arrange
        Contratacao contratacao = new Contratacao(
                UUID.randomUUID(),
                "12345678900",
                "Produto Inválido",
                BigDecimal.ONE,
                "CRIADA",
                LocalDateTime.now()
        );

        ObjectMapper mapperMock = mock(ObjectMapper.class);
        AmazonSQS sqsMock = mock(AmazonSQS.class);

        SqsProperties props = new SqsProperties();
        props.setNovaVendaUrl("http://localhost:4566/000000000000/nova-venda-criada");

        when(mapperMock.writeValueAsString(any())).thenThrow(new JsonProcessingException("Erro de mock") {});

        SqsPublisher publisher = new SqsPublisher(sqsMock, props, mapperMock);

        // Act & Assert
        assertThrows(PublicacaoEventoException.class, () -> publisher.publicar(contratacao));
    }
}
