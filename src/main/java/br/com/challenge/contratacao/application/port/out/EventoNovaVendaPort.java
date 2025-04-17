package br.com.challenge.contratacao.application.port.out;

import br.com.challenge.contratacao.domain.entity.Contratacao;

public interface EventoNovaVendaPort {
    void publicar(Contratacao contratacao);
}
