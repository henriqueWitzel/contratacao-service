package br.com.challenge.contratacao.domain.entity;

import br.com.challenge.contratacao.adapter.in.web.dto.ContratacaoRequest;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Contratacao {

    @Id
    private UUID id;

    private String cpfCliente;
    private String produto;
    private BigDecimal valor;
    private String status;
    private LocalDateTime dataCriacao;

    public Contratacao(ContratacaoRequest request) {
        this.id = UUID.randomUUID();
        this.cpfCliente = request.cpfCliente();
        this.produto = request.produto();
        this.valor = request.valor();
        this.status = "CRIADA";
        this.dataCriacao = LocalDateTime.now();
    }
}
