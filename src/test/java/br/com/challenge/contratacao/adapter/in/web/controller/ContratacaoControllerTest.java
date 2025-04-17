package br.com.challenge.contratacao.adapter.in.web.controller;

import br.com.challenge.contratacao.adapter.in.web.dto.ContratacaoRequest;
import br.com.challenge.contratacao.adapter.in.web.dto.ContratacaoResponse;
import br.com.challenge.contratacao.application.port.in.CriarContratacaoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@Tag("unit")
@WebMvcTest(ContratacaoController.class)
class ContratacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CriarContratacaoUseCase useCase;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void deveCriarNovaContratacao() throws Exception {
        UUID fakeId = UUID.randomUUID();
        Mockito.when(useCase.executar(any())).thenReturn(fakeId);

        var request = new ContratacaoRequest("12345678900","Seguro Automotivo",new BigDecimal("300.00"));

        mockMvc.perform(post("/contratacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/contratacoes/" + fakeId));
    }

    @Test
    void deveBuscarContratacaoPorId() throws Exception {
        UUID fakeId = UUID.randomUUID();

        var response = new ContratacaoResponse(
                fakeId,
                "12345678900",
                "Seguro Automotivo",
                new BigDecimal("300.00"),
                "CRIADA",
                LocalDateTime.now()
        );

        Mockito.when(useCase.buscar(fakeId)).thenReturn(response);

        mockMvc.perform(get("/contratacoes/" + fakeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpfCliente", is("12345678900")))
                .andExpect(jsonPath("$.produto", is("Seguro Automotivo")))
                .andExpect(jsonPath("$.valor", is(300.00)))
                .andExpect(jsonPath("$.status", is("CRIADA")));
    }
}
