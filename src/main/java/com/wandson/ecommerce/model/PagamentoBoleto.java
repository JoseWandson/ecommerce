package com.wandson.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@DiscriminatorValue("boleto")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class PagamentoBoleto extends Pagamento {

    @Column(name = "codigo_barras", length = 100)
    private String codigoBarras;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;
}
