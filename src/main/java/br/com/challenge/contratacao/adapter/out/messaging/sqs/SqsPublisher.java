package br.com.challenge.contratacao.adapter.out.messaging.sqs;

import br.com.challenge.contratacao.application.config.SqsProperties;
import br.com.challenge.contratacao.application.exception.PublicacaoEventoException;
import br.com.challenge.contratacao.application.port.out.EventoNovaVendaPort;
import br.com.challenge.contratacao.domain.entity.Contratacao;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Publicador de eventos para a fila do SQS.
 */
@Component
@RequiredArgsConstructor
public class SqsPublisher implements EventoNovaVendaPort {

    private final AmazonSQS amazonSQS;
    private final SqsProperties properties;
    private final ObjectMapper objectMapper;

    @Override
    public void publicar(Contratacao contratacao) {
        Assert.notNull(properties.getNovaVendaUrl(), "SQS URL não configurada");

        try {
            String mensagem = objectMapper.writeValueAsString(contratacao);
            amazonSQS.sendMessage(new SendMessageRequest(properties.getNovaVendaUrl(), mensagem));
        } catch (JsonProcessingException e) {
            throw new PublicacaoEventoException("Erro ao serializar a mensagem para o SQS", e);
        }
    }
}
