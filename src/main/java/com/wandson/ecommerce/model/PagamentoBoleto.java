package com.wandson.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PagamentoBoleto {

    @Id
    @EqualsAndHashCode.Include
    private Integer id;
    private Integer pedidoId;
    private StatusPagamento status;
    private String codigoBarras;
}
