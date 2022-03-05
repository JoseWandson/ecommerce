package com.wandson.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("boleto")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class PagamentoBoleto extends Pagamento {

    @Column(name = "codigo_barras", length = 100)
    private String codigoBarras;
}
