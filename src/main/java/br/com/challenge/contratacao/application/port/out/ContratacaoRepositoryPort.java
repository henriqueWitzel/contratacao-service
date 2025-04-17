package br.com.challenge.contratacao.application.port.out;

import br.com.challenge.contratacao.domain.entity.Contratacao;

import java.util.Optional;
import java.util.UUID;

public interface ContratacaoRepositoryPort {
    Contratacao salvar(Contratacao contratacao);
    Optional<Contratacao> buscarPorId(UUID id);
    void deletar(UUID id);
}
