package com.wandson.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "nota_fiscal")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class NotaFiscal extends EntidadeBaseInteger {

    @MapsId
    @NotNull
    @OneToOne(optional = false)
    @JoinColumn(name = "pedido_id", nullable = false, foreignKey = @ForeignKey(name = "fk_nota_fiscal_pedido"))
    private Pedido pedido;

    @Lob
    @NotEmpty
    @Column(nullable = false)
    private byte[] xml;

    @NotNull
    @PastOrPresent
    @Column(name = "data_emissao", nullable = false)
    private LocalDateTime dataEmissao;
}
