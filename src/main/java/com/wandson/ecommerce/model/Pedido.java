package com.wandson.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pedido {

    @Id
    @EqualsAndHashCode.Include
    private Integer id;
    private LocalDateTime dataPedido;
    private LocalDateTime dataConclusao;
    private Integer notaFiscalId;
    private BigDecimal total;
    private StatusPedido status;
}
