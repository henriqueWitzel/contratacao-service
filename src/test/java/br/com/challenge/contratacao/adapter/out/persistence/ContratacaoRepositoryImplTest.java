package br.com.challenge.contratacao.adapter.out.persistence;

import br.com.challenge.contratacao.domain.entity.Contratacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Tag("unit")
class ContratacaoRepositoryImplTest {

    private SpringDataJpaContratacaoRepository springDataJpaRepository;
    private ContratacaoRepositoryImpl repositoryImpl;

    @BeforeEach
    void setUp() {
        springDataJpaRepository = mock(SpringDataJpaContratacaoRepository.class);
        repositoryImpl = new ContratacaoRepositoryImpl(springDataJpaRepository);
    }

    @Test
    void deveSalvarContratacaoComSucesso() {
        Contratacao contratacao = new Contratacao(
                UUID.randomUUID(),
                "98765432100",
                "Seguro Automotivo",
                new BigDecimal("200.00"),
                "CRIADA",
                LocalDateTime.now()
        );

        when(springDataJpaRepository.save(contratacao)).thenReturn(contratacao);

        Contratacao resultado = repositoryImpl.salvar(contratacao);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getCpfCliente()).isEqualTo("98765432100");
        verify(springDataJpaRepository, times(1)).save(contratacao);
    }

    @Test
    void deveRetornarContratacaoPorId() {
        UUID id = UUID.randomUUID();
        Contratacao contratacao = new Contratacao(id, "12345678900", "Seguro Automotivo",
                new BigDecimal("150.00"), "CRIADA", LocalDateTime.now());

        when(springDataJpaRepository.findById(id)).thenReturn(Optional.of(contratacao));

        Optional<Contratacao> resultado = repositoryImpl.buscarPorId(id);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(id);
        verify(springDataJpaRepository, times(1)).findById(id);
    }

    @Test
    void deveDeletarContratacaoPorId() {
        UUID id = UUID.randomUUID();

        repositoryImpl.deletar(id);

        verify(springDataJpaRepository, times(1)).deleteById(id);
    }

}
