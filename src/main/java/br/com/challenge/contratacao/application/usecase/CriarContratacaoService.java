package br.com.challenge.contratacao.application.usecase;

import br.com.challenge.contratacao.adapter.in.web.dto.ContratacaoRequest;
import br.com.challenge.contratacao.adapter.in.web.dto.ContratacaoResponse;
import br.com.challenge.contratacao.application.exception.RecursoNaoEncontradoException;
import br.com.challenge.contratacao.application.port.in.CriarContratacaoUseCase;
import br.com.challenge.contratacao.application.port.out.ContratacaoRepositoryPort;
import br.com.challenge.contratacao.application.port.out.EventoNovaVendaPort;
import br.com.challenge.contratacao.domain.entity.Contratacao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CriarContratacaoService implements CriarContratacaoUseCase {

    private final ContratacaoRepositoryPort repository;
    private final EventoNovaVendaPort publisher;

    @Override
    public UUID executar(ContratacaoRequest request) {
        Contratacao nova = new Contratacao(request);
        repository.salvar(nova);
        publisher.publicar(nova);
        return nova.getId();
    }

    @Override
    public ContratacaoResponse buscar(UUID id) {
        return repository.buscarPorId(id)
                .map(ContratacaoResponse::new)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Contratação não encontrada"));
    }
}
