package com.wandson.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "item_pedido")
@SqlResultSetMapping(name = "item_pedido-produto.ItemPedido-Produto",
        entities = {@EntityResult(entityClass = ItemPedido.class), @EntityResult(entityClass = Produto.class)})
public class ItemPedido {

    @EmbeddedId
    private ItemPedidoId id;

    @Version
    private Integer versao;

    @NotNull
    @MapsId("pedidoId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id", nullable = false, foreignKey = @ForeignKey(name = "fk_item_pedido_pedido"))
    private Pedido pedido;

    @NotNull
    @MapsId("produtoId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false, foreignKey = @ForeignKey(name = "fk_item_pedido_produto"))
    private Produto produto;

    @NotNull
    @Positive
    @Column(name = "preco_produto", nullable = false)
    private BigDecimal precoProduto;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer quantidade;
}
