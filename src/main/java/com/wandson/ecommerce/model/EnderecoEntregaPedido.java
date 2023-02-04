package com.wandson.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class EnderecoEntregaPedido {

    @NotNull
    @Column(length = 9)
    @Pattern(regexp = "[0-9]{5}-[0-9]{3}")
    private String cep;

    @NotBlank
    @Column(length = 100)
    private String logradouro;

    @NotBlank
    @Column(length = 10)
    private String numero;

    @Column(length = 50)
    private String complemento;

    @NotBlank
    @Column(length = 50)
    private String bairro;

    @NotBlank
    @Column(length = 50)
    private String cidade;

    @NotNull
    @Size(max = 2, min = 2)
    @Column(length = 2)
    private String estado;
}
