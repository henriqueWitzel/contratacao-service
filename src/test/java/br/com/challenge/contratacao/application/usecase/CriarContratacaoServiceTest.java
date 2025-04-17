package br.com.challenge.contratacao.application.usecase;

import br.com.challenge.contratacao.adapter.in.web.dto.ContratacaoRequest;
import br.com.challenge.contratacao.application.port.out.ContratacaoRepositoryPort;
import br.com.challenge.contratacao.application.port.out.EventoNovaVendaPort;
import br.com.challenge.contratacao.domain.entity.Contratacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
class CriarContratacaoServiceTest {

    private ContratacaoRepositoryPort repository;
    private EventoNovaVendaPort eventoNovaVendaPort;
    private CriarContratacaoService service;

    @BeforeEach
    void setUp() {
        // Mocka as dependências necessárias para a service
        repository = mock(ContratacaoRepositoryPort.class);
        eventoNovaVendaPort = mock(EventoNovaVendaPort.class);
        service = new CriarContratacaoService(repository, eventoNovaVendaPort);
    }

    @Test
    void deveCriarContratacaoComSucesso() {
        // Prepara os dados de entrada (request)
        ContratacaoRequest request = new ContratacaoRequest("12345678900","Seguro Automotivo",new BigDecimal("200.00"));

        // Captura o objeto salvo no repositório
        ArgumentCaptor<Contratacao> captor = ArgumentCaptor.forClass(Contratacao.class);
        when(repository.salvar(any())).thenAnswer(i -> i.getArguments()[0]);

        UUID id = service.executar(request);

        assertNotNull(id);
        verify(repository).salvar(captor.capture());
        Contratacao contratacaoSalva = captor.getValue();
        assertEquals("12345678900", contratacaoSalva.getCpfCliente());
        assertEquals("Seguro Automotivo", contratacaoSalva.getProduto());
        assertEquals(new BigDecimal("200.00"), contratacaoSalva.getValor());

        verify(eventoNovaVendaPort).publicar(contratacaoSalva);
    }

    @Test
    void deveBuscarContratacaoPorId() {
        // Cria uma contratação mockada
        UUID id = UUID.randomUUID();
        Contratacao c = new Contratacao(UUID.randomUUID(), "12345678900", "Seguro Automotivo", new BigDecimal("200.00"), "CRIADA", LocalDateTime.now());
        when(repository.buscarPorId(id)).thenReturn(Optional.of(c));

        var response = service.buscar(id);

        assertNotNull(response);
        assertEquals("12345678900", response.cpfCliente());
        assertEquals("Seguro Automotivo", response.produto());
        assertEquals(new BigDecimal("200.00"), response.valor());
    }

    @Test
    void deveLancarExcecaoQuandoContratacaoNaoExistir() {
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        // Garante que uma exceção de Runtime é lançada caso não encontre
        assertThrows(RuntimeException.class, () -> service.buscar(id));
    }
}


