package com.wandson.ecommerce.model;

import com.wandson.ecommerce.dto.ProdutoDTO;
import com.wandson.ecommerce.listener.GenericoListener;
import com.wandson.ecommerce.model.converter.BooleanToSimNaoConverter;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EntityResult;
import jakarta.persistence.FieldResult;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@EntityListeners(GenericoListener.class)
@NamedNativeQuery(name = "produto_loja.listar",
        query = "select id, nome, descricao, data_criacao, data_ultima_atualizacao, preco, foto from produto_loja",
        resultClass = Produto.class)
@NamedNativeQuery(name = "ecm_produto.listar", query = "select * from ecm_produto", resultSetMapping = "ecm_produto.Produto")
@NamedQuery(name = "Produto.listar", query = "select p from Produto p")
@NamedQuery(name = "Produto.listarPorCategoria",
        query = "select p from Produto p where exists (select 1 from Categoria c2 join c2.produtos p2 where p2 = p and c2.id = :categoria)")
@SqlResultSetMapping(name = "produto_loja.Produto", entities = @EntityResult(entityClass = Produto.class))
@SqlResultSetMapping(name = "ecm_produto.Produto", entities = @EntityResult(entityClass = Produto.class, fields = {
        @FieldResult(name = "id", column = "prd_id"), @FieldResult(name = "nome", column = "prd_nome"),
        @FieldResult(name = "descricao", column = "prd_descricao"), @FieldResult(name = "preco", column = "prd_preco"),
        @FieldResult(name = "foto", column = "prd_foto"),
        @FieldResult(name = "dataCriacao", column = "prd_data_criacao"),
        @FieldResult(name = "dataUltimaAtualizacao", column = "prd_data_ultima_atualizacao")}))
@SqlResultSetMapping(name = "ecm_produto.ProdutoDTO", classes = @ConstructorResult(targetClass = ProdutoDTO.class, columns = {
        @ColumnResult(name = "prd_id", type = Integer.class), @ColumnResult(name = "prd_nome", type = String.class)}))
@Table(name = "produto", uniqueConstraints = @UniqueConstraint(name = "unq_produto_nome", columnNames = "nome"),
        indexes = @Index(name = "idx_produto_nome", columnList = "nome"))
public class Produto extends EntidadeBaseInteger {

    @NotBlank
    @Column(length = 100, nullable = false)
    private String nome;

    private String descricao;

    @Positive
    private BigDecimal preco;

    @NotNull
    @PastOrPresent
    @Column(name = "data_criacao", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;

    @PastOrPresent
    @Column(name = "data_ultima_atualizacao", insertable = false)
    private LocalDateTime dataUltimaAtualizacao;

    @Lob
    private byte[] foto;

    @NotNull
    @Column(length = 3, nullable = false)
    @Convert(converter = BooleanToSimNaoConverter.class)
    private Boolean ativo;

    @OneToOne(mappedBy = "produto")
    private Estoque estoque;

    @ManyToMany
    @JoinTable(name = "produto_categoria",
            joinColumns = @JoinColumn(name = "produto_id", nullable = false,
                    foreignKey = @ForeignKey(name = "fk_produto_categoria_produto")),
            inverseJoinColumns = @JoinColumn(name = "categoria_id", nullable = false,
                    foreignKey = @ForeignKey(name = "fk_produto_categoria_categoria")))
    private List<Categoria> categorias;

    @ElementCollection
    @Column(name = "tag", length = 50, nullable = false)
    @CollectionTable(name = "produto_tag",
            joinColumns = @JoinColumn(name = "produto_id", nullable = false,
                    foreignKey = @ForeignKey(name = "fk_produto_tag_produto")))
    private List<String> tags;

    @ElementCollection
    @CollectionTable(name = "produto_atributo",
            joinColumns = @JoinColumn(name = "produto_id", nullable = false,
                    foreignKey = @ForeignKey(name = "fk_produto_atributo_produto")))
    private List<Atributo> atributos;

    public Produto() {
        ativo = Boolean.FALSE;
    }
}
