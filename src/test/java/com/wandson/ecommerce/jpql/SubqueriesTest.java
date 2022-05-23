package com.wandson.ecommerce.jpql;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Cliente;
import com.wandson.ecommerce.model.Pedido;
import com.wandson.ecommerce.model.Produto;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class SubqueriesTest extends EntityManagerTest {

    @Test
    void pesquisarSubqueries_bonsClientesVersao2() {
        var jpql = "select c from Cliente c where 500 < (select sum(p.total) from Pedido p where p.cliente = c)";

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(jpql, Cliente.class);

        List<Cliente> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(c -> System.out.println("ID: " + c.getId() + ", Nome: " + c.getNome()));
    }

    @Test
    void pesquisarSubqueries_BonsClientesVersao1() {
        var jpql = "select c from Cliente c where 500 < (select sum(p.total) from c.pedidos p)";

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(jpql, Cliente.class);

        List<Cliente> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(c -> System.out.println("ID: " + c.getId() + ", Nome: " + c.getNome()));
    }

    @Test
    void pesquisarSubqueries_todosOsPedidosAcimaDaMediaDeVendas() {
        var jpql = "select p from Pedido p where p.total > (select avg(total) from Pedido)";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);

        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", Total: " + p.getTotal()));
    }

    @Test
    void pesquisarSubqueries_oProdutoOuOsProdutosMaisCarosDaBase() {
        var jpql = "select p from Produto p where p.preco = (select max(preco) from Produto)";

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);

        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", Preço: " + p.getPreco()));
    }

    @Test
    void pesquisarComIN() {
        var jpql = """
                select p
                from Pedido p
                where p.id in (select p2.id
                               from ItemPedido i2
                                        join i2.pedido p2
                                        join i2.produto pro2
                               where pro2.preco > 100)""";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);

        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.println("ID: " + obj.getId()));
    }

    @Test
    void pesquisarComExists() {
        var jpql = """
                select p
                from Produto p
                where exists(select 1
                             from ItemPedido ip2
                                      join ip2.produto p2
                             where p2 = p)""";

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);

        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.println("ID: " + obj.getId()));
    }
}