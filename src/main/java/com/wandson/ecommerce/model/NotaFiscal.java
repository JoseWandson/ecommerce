package com.wandson.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NotaFiscal {

    @Id
    @EqualsAndHashCode.Include
    private Integer id;
    private Integer pedidoId;
    private String xml;
    private Date dataEmissao;
}
