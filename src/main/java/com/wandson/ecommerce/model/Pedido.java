package com.wandson.ecommerce.model;

import com.wandson.ecommerce.listener.GenericoListener;
import com.wandson.ecommerce.listener.GerarNotaFiscalListener;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Log
@Entity
@Getter
@Setter
@Table(name = "pedido")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners({GerarNotaFiscalListener.class, GenericoListener.class})
public class Pedido {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_ultima_atualizacao")
    private LocalDateTime dataUltimaAtualizacao;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @OneToOne(mappedBy = "pedido")
    private NotaFiscal notaFiscal;
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @Embedded
    private EnderecoEntregaPedido enderecoEntrega;

    @OneToOne(mappedBy = "pedido")
    private PagamentoCartao pagamento;

    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itens;

    public boolean isPago() {
        return StatusPedido.PAGO.equals(status);
    }

    @PrePersist
    private void aoPersistir() {
        dataCriacao = LocalDateTime.now();
        calcularTotal();
    }

    @PreUpdate
    private void aoAtualizar() {
        dataUltimaAtualizacao = LocalDateTime.now();
        calcularTotal();
    }

    @PostPersist
    private void aposPersistir() {
        log.info("Após persistir Pedido.");
    }

    @PostUpdate
    private void aposAtualizar() {
        log.info("Após atualizar Pedido.");
    }

    @PreRemove
    private void aoRemover() {
        log.info("Antes de remover Pedido.");
    }

    @PostRemove
    private void aposRemover() {
        log.info("Após remover Pedido.");
    }

    @PostLoad
    private void aoCarregar() {
        log.info("Após carregar o Pedido.");
    }

    private void calcularTotal() {
        if (Objects.nonNull(itens)) {
            total = itens.stream().map(ItemPedido::getPrecoProduto).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
}
