package com.wandson.ecommerce.criteria;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.dto.ProdutoDTO;
import com.wandson.ecommerce.model.Cliente;
import com.wandson.ecommerce.model.Cliente_;
import com.wandson.ecommerce.model.Pedido;
import com.wandson.ecommerce.model.Pedido_;
import com.wandson.ecommerce.model.Produto;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

class BasicoCriteriaTest extends EntityManagerTest {

    @Test
    void buscarPorIdentificador() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), 1));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);

        Pedido pedido = typedQuery.getSingleResult();
        Assertions.assertNotNull(pedido);
    }

    @Test
    void selecionarUmAtributoParaRetorno() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> criteriaQuery = criteriaBuilder.createQuery(BigDecimal.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.select(root.get("total"));
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), 1));

        TypedQuery<BigDecimal> typedQuery = entityManager.createQuery(criteriaQuery);

        BigDecimal total = typedQuery.getSingleResult();
        Assertions.assertEquals(new BigDecimal("2398.00"), total);
    }

    @Test
    void retornarTodosOsProdutos() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(root);

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void projetarOResultado() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.multiselect(root.get("id"), root.get("nome"));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println("ID: " + arr[0] + ", Nome: " + arr[1]));
    }

    @Test
    void projetarOResultadoTuple() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(criteriaBuilder.tuple(root.get("id").alias("id"), root.get("nome").alias("nome")));

        TypedQuery<Tuple> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Tuple> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(t -> System.out.println("ID: " + t.get("id") + ", Nome: " + t.get("nome")));
    }

    @Test
    void projetarOResultadoDTO() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProdutoDTO> criteriaQuery = criteriaBuilder.createQuery(ProdutoDTO.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(criteriaBuilder.construct(ProdutoDTO.class, root.get("id"), root.get("nome")));

        TypedQuery<ProdutoDTO> typedQuery = entityManager.createQuery(criteriaQuery);

        List<ProdutoDTO> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(dto -> System.out.println("ID: " + dto.getId() + ", Nome: " + dto.getNome()));
    }

    @Test
    void ordenarResultados() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        Root<Cliente> root = criteriaQuery.from(Cliente.class);

        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(Cliente_.nome)));

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Cliente> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(c -> System.out.println(c.getId() + ", " + c.getNome()));
    }

    @Test
    void usarDistinct() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        root.join(Pedido_.itens);

        criteriaQuery.select(root);
        criteriaQuery.distinct(true);

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId()));
    }
}
