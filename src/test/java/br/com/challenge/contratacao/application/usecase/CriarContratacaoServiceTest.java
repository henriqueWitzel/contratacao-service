package br.com.challenge.contratacao.application.usecase;

import br.com.challenge.contratacao.adapter.in.web.dto.ContratacaoRequest;
import br.com.challenge.contratacao.application.exception.RecursoNaoEncontradoException;
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
        repository = mock(ContratacaoRepositoryPort.class);
        eventoNovaVendaPort = mock(EventoNovaVendaPort.class);
        service = new CriarContratacaoService(repository, eventoNovaVendaPort);
    }

    @Test
    void deveCriarContratacaoComSucesso() {
        ContratacaoRequest request = new ContratacaoRequest(
                "12345678900",
                "Seguro Automotivo",
                new BigDecimal("200.00"),
                "Hyundai HB20",
                2021,
                "Sudeste"
        );

        ArgumentCaptor<Contratacao> captor = ArgumentCaptor.forClass(Contratacao.class);
        when(repository.salvar(any())).thenAnswer(i -> i.getArguments()[0]);

        UUID id = service.executar(request);

        assertNotNull(id);
        verify(repository).salvar(captor.capture());

        Contratacao contratacaoSalva = captor.getValue();
        assertEquals("12345678900", contratacaoSalva.getCpfCliente());
        assertEquals("Seguro Automotivo", contratacaoSalva.getProduto());
        assertEquals(new BigDecimal("200.00"), contratacaoSalva.getValor());
        assertEquals("Hyundai HB20", contratacaoSalva.getModeloVeiculo());
        assertEquals(2021, contratacaoSalva.getAnoFabricacao());
        assertEquals("Sudeste", contratacaoSalva.getRegiaoContratacao());

        verify(eventoNovaVendaPort).publicar(contratacaoSalva);
    }

    @Test
    void deveBuscarContratacaoPorId() {
        UUID id = UUID.randomUUID();
        Contratacao c = new Contratacao(
                id,
                "12345678900",
                "Seguro Automotivo",
                new BigDecimal("200.00"),
                "CRIADA",
                LocalDateTime.now(),
                "Hyundai HB20",
                2021,
                "Sudeste"
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(c));

        var response = service.buscar(id);

        assertNotNull(response);
        assertEquals("12345678900", response.cpfCliente());
        assertEquals("Seguro Automotivo", response.produto());
        assertEquals(new BigDecimal("200.00"), response.valor());
        assertEquals("Hyundai HB20", response.modeloVeiculo());
        assertEquals(2021, response.anoFabricacao());
        assertEquals("Sudeste", response.regiaoContratacao());
    }

    @Test
    void deveLancarExcecaoQuandoContratacaoNaoExistir() {
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscar(id));
    }
}