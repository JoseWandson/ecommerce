package com.wandson.ecommerce.model;

import com.wandson.ecommerce.listener.GenericoListener;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@EntityListeners({GenericoListener.class})
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Table(name = "produto", uniqueConstraints = @UniqueConstraint(name = "unq_nome", columnNames = "nome"), indexes = @Index(name = "idx_nome", columnList = "nome"))
public class Produto extends EntidadeBaseInteger {

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(columnDefinition = "varchar(275) default 'descricao'")
    private String descricao;

    private BigDecimal preco;

    @Column(name = "data_criacao", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_ultima_atualizacao", insertable = false)
    private LocalDateTime dataUltimaAtualizacao;

    @Lob
    private byte[] foto;

    @OneToOne(mappedBy = "produto")
    private Estoque estoque;

    @ManyToMany
    @JoinTable(name = "produto_categoria", joinColumns = @JoinColumn(name = "produto_id"), inverseJoinColumns = @JoinColumn(name = "categoria_id"))
    private List<Categoria> categorias;

    @ElementCollection
    @Column(name = "tag", length = 50, nullable = false)
    @CollectionTable(name = "produto_tag", joinColumns = @JoinColumn(name = "produto_id"))
    private List<String> tags;

    @ElementCollection
    @CollectionTable(name = "produto_atributo", joinColumns = @JoinColumn(name = "produto_id"))
    private List<Atributo> atributos;
}
