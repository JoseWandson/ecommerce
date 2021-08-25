package com.wandson.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemPedido {

    @Id
    @EqualsAndHashCode.Include
    private Integer id;
    private Integer pedidoId;
    private Integer produtoId;
    private BigDecimal precoProduto;
    private Integer quantidade;
}
