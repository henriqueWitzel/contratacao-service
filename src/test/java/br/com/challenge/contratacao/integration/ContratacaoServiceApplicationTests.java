package br.com.challenge.contratacao.integration;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
@SpringBootTest
class ContratacaoServiceApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        // Garante que o contexto foi carregado
        assertNotNull(context, "O contexto da aplicação não foi carregado corretamente");
    }

    @Test
    void deveConterBeanDoController() {
        // Verifica se o bean do controller está presente no contexto
        boolean temController = context.containsBean("contratacaoController");
        assertTrue(temController, "Bean contratacaoController não foi carregado no contexto");
    }
}