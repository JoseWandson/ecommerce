package com.wandson.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("cartao")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class PagamentoCartao extends Pagamento {

    @NotBlank
    @Column(name = "numero_cartao", length = 50)
    private String numeroCartao;
}
