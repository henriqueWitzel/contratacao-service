package br.com.challenge.contratacao.adapter.in.web.controller;

import br.com.challenge.contratacao.adapter.in.web.dto.ContratacaoRequest;
import br.com.challenge.contratacao.adapter.in.web.dto.ContratacaoResponse;
import br.com.challenge.contratacao.application.port.in.CriarContratacaoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/contratacoes")
@RequiredArgsConstructor
public class ContratacaoController {

    private final CriarContratacaoUseCase criarContratacaoUseCase;

    @Operation(
            summary = "Cria uma nova contratação",
            description = "Recebe os dados de uma contratação e envia um evento para a fila SQS após salvar no banco"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contratação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> contratar(@RequestBody @Valid ContratacaoRequest request) {
        log.info("Recebida requisição de contratação para CPF: {}", request.cpfCliente());

        UUID id = criarContratacaoUseCase.executar(request);

        log.info("Contratação criada com sucesso. ID: {}", id);
        return ResponseEntity.created(URI.create("/contratacoes/" + id)).build();
    }

    @Operation(
            summary = "Busca uma contratação por ID",
            description = "Retorna os dados da contratação armazenada, dado um ID válido"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contratação encontrada",
                    content = @Content(schema = @Schema(implementation = ContratacaoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Contratação não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContratacaoResponse> buscar(@PathVariable UUID id) {
        log.info("Recebida requisição para buscar contratação com ID: {}", id);

        ContratacaoResponse response = criarContratacaoUseCase.buscar(id);

        log.debug("Contratação encontrada: {}", response);
        return ResponseEntity.ok(response);
    }
}
