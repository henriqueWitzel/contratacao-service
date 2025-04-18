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
        amazonSQS = mock(AmazonSQS.class);

        SqsProperties properties = new SqsProperties();
        properties.setNovaVendaUrl("http://localhost:4566/000000000000/nova-venda-criada");

        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        publisher = new SqsPublisher(amazonSQS, properties, objectMapper);
    }

    @Test
    void devePublicarMensagemComSucesso() {
        Contratacao contratacao = new Contratacao(
                UUID.randomUUID(),
                "12345678900",
                "Seguro Automotivo",
                new BigDecimal("150.00"),
                "CRIADA",
                LocalDateTime.now(),
                "Hyundai HB20",
                2021,
                "Sudeste"
        );

        publisher.publicar(contratacao);

        verify(amazonSQS, times(1)).sendMessage(any(SendMessageRequest.class));
    }

    @Test
    void deveLancarErroQuandoUrlNaoForConfigurada() {
        SqsProperties semUrl = new SqsProperties();

        SqsPublisher publisherComErro = new SqsPublisher(amazonSQS, semUrl, objectMapper);

        Contratacao contratacao = new Contratacao(
                UUID.randomUUID(),
                "00000000000",
                "Teste",
                BigDecimal.ONE,
                "CRIADA",
                LocalDateTime.now(),
                "Celta",
                2005,
                "Sul"
        );

        assertThrows(IllegalArgumentException.class, () -> publisherComErro.publicar(contratacao));
    }

    @Test
    void deveLancarPublicacaoEventoExceptionQuandoFalharSerializacao() throws JsonProcessingException {
        Contratacao contratacao = new Contratacao(
                UUID.randomUUID(),
                "12345678900",
                "Produto Inválido",
                BigDecimal.ONE,
                "CRIADA",
                LocalDateTime.now(),
                "Fusca",
                1970,
                "Centro-Oeste"
        );

        ObjectMapper mapperMock = mock(ObjectMapper.class);
        AmazonSQS sqsMock = mock(AmazonSQS.class);

        SqsProperties props = new SqsProperties();
        props.setNovaVendaUrl("http://localhost:4566/000000000000/nova-venda-criada");

        when(mapperMock.writeValueAsString(any())).thenThrow(new JsonProcessingException("Erro de mock") {});

        SqsPublisher newPublisher = new SqsPublisher(sqsMock, props, mapperMock);

        assertThrows(PublicacaoEventoException.class, () -> newPublisher.publicar(contratacao));
    }
}