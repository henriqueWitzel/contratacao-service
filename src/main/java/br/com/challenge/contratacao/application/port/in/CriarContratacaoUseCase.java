package br.com.challenge.contratacao.application.port.in;

import br.com.challenge.contratacao.adapter.in.web.dto.ContratacaoRequest;
import br.com.challenge.contratacao.adapter.in.web.dto.ContratacaoResponse;

import java.util.UUID;

public interface CriarContratacaoUseCase {
    UUID executar(ContratacaoRequest request);
    ContratacaoResponse buscar(UUID id);
}