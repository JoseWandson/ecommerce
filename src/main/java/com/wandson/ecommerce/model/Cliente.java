package com.wandson.ecommerce.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@SecondaryTable(name = "cliente_detalhe", pkJoinColumns = @PrimaryKeyJoinColumn(name = "cliente_id"))
@Table(name = "cliente", uniqueConstraints = @UniqueConstraint(name = "unq_cpf", columnNames = "cpf"), indexes = @Index(name = "idx_nome", columnList = "nome"))
public class Cliente extends EntidadeBaseInteger {

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(length = 14, nullable = false)
    private String cpf;

    @Transient
    private String primeiroNome;

    @Column(table = "cliente_detalhe", name = "data_nascimento")
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(table = "cliente_detalhe", length = 30, nullable = false)
    private SexoCliente sexo;

    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos;

    @ElementCollection
    @Column(name = "descricao")
    @MapKeyColumn(name = "tipo")
    @CollectionTable(name = "cliente_contato", joinColumns = @JoinColumn(name = "cliente_id"))
    private Map<String, String> contatos;

    @PostLoad
    private void configurarPrimeiroNome() {
        if (Objects.nonNull(nome) && !nome.isBlank()) {
            int index = nome.indexOf(" ");
            if (index > 1) {
                primeiroNome = nome.substring(0, index);
            }
        }
    }
}
