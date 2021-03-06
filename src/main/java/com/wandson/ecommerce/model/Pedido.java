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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@EntityListeners({GerarNotaFiscalListener.class, GenericoListener.class})
public class Pedido extends EntidadeBaseInteger {

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false, foreignKey = @ForeignKey(name = "fk_pedido_cliente"))
    private Cliente cliente;

    @Column(name = "data_criacao", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_ultima_atualizacao", insertable = false)
    private LocalDateTime dataUltimaAtualizacao;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @OneToOne(mappedBy = "pedido")
    private NotaFiscal notaFiscal;

    @Column(nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private StatusPedido status;

    @Embedded
    private EnderecoEntregaPedido enderecoEntrega;

    @OneToOne(mappedBy = "pedido")
    private Pagamento pagamento;

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
        if (Objects.isNull(itens)) {
            total = BigDecimal.ZERO;
        } else {
            total = itens.stream().map(i -> new BigDecimal(i.getQuantidade()).multiply(i.getPrecoProduto())).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
}
