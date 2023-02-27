package com.wandson.ecommerce.model;

import com.wandson.ecommerce.listener.GenericoListener;
import com.wandson.ecommerce.listener.GerarNotaFiscalListener;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
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
@NamedEntityGraph(
        name = "Pedido.dadosEssenciais",
        attributeNodes = {
                @NamedAttributeNode("dataCriacao"),
                @NamedAttributeNode("status"),
                @NamedAttributeNode("total"),
                @NamedAttributeNode(
                        value = "cliente",
                        subgraph = "cli"
                )
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "cli",
                        attributeNodes = {
                                @NamedAttributeNode("nome"),
                                @NamedAttributeNode("cpf")
                        }
                )
        }
)
public class Pedido extends EntidadeBaseInteger {

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false, foreignKey = @ForeignKey(name = "fk_pedido_cliente"))
    private Cliente cliente;

    @NotNull
    @PastOrPresent
    @Column(name = "data_criacao", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;

    @PastOrPresent
    @Column(name = "data_ultima_atualizacao", insertable = false)
    private LocalDateTime dataUltimaAtualizacao;

    @PastOrPresent
    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @OneToOne(mappedBy = "pedido")
    private NotaFiscal notaFiscal;

    @NotNull
    @Positive
    @Column(nullable = false)
    private BigDecimal total;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private StatusPedido status;

    @Embedded
    private EnderecoEntregaPedido enderecoEntrega;

    @OneToOne(mappedBy = "pedido")
    private Pagamento pagamento;

    @NotEmpty
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
