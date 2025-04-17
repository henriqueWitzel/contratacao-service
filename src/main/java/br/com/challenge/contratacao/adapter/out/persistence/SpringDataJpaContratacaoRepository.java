package br.com.challenge.contratacao.adapter.out.persistence;

import br.com.challenge.contratacao.domain.entity.Contratacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataJpaContratacaoRepository extends JpaRepository<Contratacao, UUID> {
}
