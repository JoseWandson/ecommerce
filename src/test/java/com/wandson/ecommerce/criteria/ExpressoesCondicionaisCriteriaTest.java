package com.wandson.ecommerce.criteria;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Cliente;
import com.wandson.ecommerce.model.Cliente_;
import com.wandson.ecommerce.model.Pedido;
import com.wandson.ecommerce.model.Pedido_;
import com.wandson.ecommerce.model.Produto;
import com.wandson.ecommerce.model.Produto_;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

class ExpressoesCondicionaisCriteriaTest extends EntityManagerTest {

    @Test
    void usarExpressaoCondicionalLike() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        Root<Cliente> root = criteriaQuery.from(Cliente.class);

        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.like(root.get(Cliente_.nome), "%a%"));

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Cliente> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void usarIsEmpty() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.isNotEmpty(root.get(Produto_.categorias)));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void usarIsNull() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.isNull(root.get(Produto_.foto)));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void usarMaiorMenor() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.greaterThanOrEqualTo(root.get(Produto_.preco), new BigDecimal(799)),
                criteriaBuilder.lessThanOrEqualTo(root.get(Produto_.preco), new BigDecimal(3500)));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p ->
                System.out.println("ID: " + p.getId() + ", Nome: " + p.getNome() + ", Preço: " + p.getPreco()));
    }

    @Test
    void usarMaiorMenorComDatas() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder
                .greaterThanOrEqualTo(root.get(Pedido_.dataCriacao), LocalDateTime.now().minusDays(3)));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void usarBetween() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.between(root.get(Pedido_.dataCriacao),
                LocalDateTime.now().minusDays(5).truncatedTo(ChronoUnit.DAYS), LocalDateTime.now()));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", Total: " + p.getTotal()));
    }

    @Test
    void usarExpressaoDiferente() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.notEqual(root.get(Pedido_.total), new BigDecimal(499)));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", Total: " + p.getTotal()));
    }

    @Test
    void usarExpressaoCase() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.multiselect(
                root.get(Pedido_.id),
                criteriaBuilder.selectCase(root.get(Pedido_.pagamento).type().as(String.class))
                        .when("boleto", "Foi pago com boleto")
                        .when("cartao", "Foi pago com cartão")
                        .otherwise("Não identificado")
        );

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    void usarExpressaoIN02() {
        Cliente cliente01 = entityManager.find(Cliente.class, 1);

        var cliente02 = new Cliente();
        cliente02.setId(2);

        List<Cliente> clientes = Arrays.asList(cliente01, cliente02);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.select(root);
        criteriaQuery.where(root.get(Pedido_.cliente).in(clientes));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void usarExpressaoIN01() {
        List<Integer> ids = Arrays.asList(1, 3, 4, 6);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.select(root);
        criteriaQuery.where(root.get(Pedido_.id).in(ids));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }


}