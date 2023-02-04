package com.wandson.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    @Column(name = "codigo_barras", length = 100)
    private String codigoBarras;

    @NotNull
    @FutureOrPresent
    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;
}
