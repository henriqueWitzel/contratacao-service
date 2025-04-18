package br.com.challenge.contratacao.application.usecase;

import br.com.challenge.contratacao.adapter.in.web.dto.ContratacaoRequest;
import br.com.challenge.contratacao.adapter.in.web.dto.ContratacaoResponse;
import br.com.challenge.contratacao.application.exception.RecursoNaoEncontradoException;
import br.com.challenge.contratacao.application.port.in.CriarContratacaoUseCase;
import br.com.challenge.contratacao.application.port.out.ContratacaoRepositoryPort;
import br.com.challenge.contratacao.application.port.out.EventoNovaVendaPort;
import br.com.challenge.contratacao.domain.entity.Contratacao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CriarContratacaoService implements CriarContratacaoUseCase {

    private final ContratacaoRepositoryPort repository;
    private final EventoNovaVendaPort publisher;

    @Override
    public UUID executar(ContratacaoRequest request) {
        log.info("Iniciando criação de nova contratação para CPF: {}", request.cpfCliente());
        log.debug("Payload recebido: {}", request);

        Contratacao nova = new Contratacao(request);
        repository.salvar(nova);
        log.info("Contratação persistida com ID: {}, Produto: {}, Região: {}",
                nova.getId(), nova.getProduto(), nova.getRegiaoContratacao());

        publisher.publicar(nova);
        log.info("Evento de nova venda publicado para ID: {}", nova.getId());

        return nova.getId();
    }

    @Override
    public ContratacaoResponse buscar(UUID id) {
        log.info("Buscando contratação com ID: {}", id);

        return repository.buscarPorId(id)
                .map(contratacao -> {
                    log.info("Contratação encontrada para ID: {}", id);
                    log.debug("Dados da contratação: {}", contratacao);
                    return new ContratacaoResponse(contratacao);
                })
                .orElseThrow(() -> {
                    log.warn("Contratação não encontrada para ID: {}", id);
                    return new RecursoNaoEncontradoException("Contratação não encontrada");
                });
    }
}