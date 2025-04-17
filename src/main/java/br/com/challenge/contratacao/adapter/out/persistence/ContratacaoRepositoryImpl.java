package br.com.challenge.contratacao.adapter.out.persistence;

import br.com.challenge.contratacao.application.port.out.ContratacaoRepositoryPort;
import br.com.challenge.contratacao.domain.entity.Contratacao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ContratacaoRepositoryImpl implements ContratacaoRepositoryPort {

    private final SpringDataJpaContratacaoRepository repository;

    @Override
    public Contratacao salvar(Contratacao contratacao) {
        return repository.save(contratacao);
    }

    @Override
    public Optional<Contratacao> buscarPorId(UUID id) {
        return repository.findById(id);
    }

    @Override
    public void deletar(UUID id) { repository.deleteById(id); }
}
