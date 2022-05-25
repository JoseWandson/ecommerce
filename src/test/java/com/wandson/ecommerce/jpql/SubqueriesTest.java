package com.wandson.ecommerce.jpql;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Cliente;
import com.wandson.ecommerce.model.Pedido;
import com.wandson.ecommerce.model.Produto;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

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
    void perquisarComSubquery_clientesCom2OuMaisPedidos() {
        var jpql = "select c from Cliente c where (select count(cliente) from Pedido where cliente = c) >= 2";

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(jpql, Cliente.class);

        List<Cliente> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.println("ID: " + obj.getId()));
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
    void pesquisarComIN_pedidosComProdutoDaCategoria2() {
        String jpql = """
                select p
                from Pedido p
                where p.id in (select p2.id
                               from ItemPedido i2
                                        join i2.pedido p2
                                        join i2.produto pro2
                                        join pro2.categorias c2
                               where c2.id = 2)""";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);

        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.println("ID: " + obj.getId()));
    }

    @ParameterizedTest
    @MethodSource("getJpqlsComExists")
    void pesquisarComExists(String jpql) {
        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);

        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.println("ID: " + obj.getId()));
    }

    @ParameterizedTest
    @MethodSource("getJpqlsComAll")
    void pesquisarComAll(String jpql) {
        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);

        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.println("ID: " + obj.getId()));
    }

    @Test
    void pesquisarComAny_todosOsProdutosQueJaForamVendidosPorUmPrecoDiferenteDoAtual() {
        var jpql = """
                select p
                from Produto p
                where p.preco <> ANY (select precoProduto from ItemPedido where produto = p)""";

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);

        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.println("ID: " + obj.getId()));
    }

    @Test
    void pesquisarComAny_todosOsProdutosQueJaForamVendidosPeloMenosUmaVezPeloPrecoAtual() {
        var jpql = """
                select p
                from Produto p
                where p.preco = ANY (select precoProduto from ItemPedido where produto = p)""";

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);

        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.println("ID: " + obj.getId()));
    }

    private static Stream<Arguments> getJpqlsComExists() {
//         Todos produtos que tenham pedidos
        var jpql1 = """
                select p
                from Produto p
                where exists(select 1
                             from ItemPedido ip2
                                      join ip2.produto p2
                             where p2 = p)""";

//         Produtos não vendidos pelo preço atual
        var jpql2 = """
                select p
                from Produto p
                where exists(select 1 from ItemPedido where produto = p and precoProduto <> p.preco)""";

        return Stream.of(arguments(jpql1), arguments(jpql2));
    }

    private static Stream<Arguments> getJpqlsComAll() {
//         Todos os produtos que nao foram vendidos mas depois encareceram
        var jpql1 = """
                select p
                from Produto p
                where p.preco > ALL (select precoProduto from ItemPedido where produto = p)""";

//         Todos os produtos que sempre foram vendidos pelo preco atual
        var jpql2 = """
                select p
                from Produto p
                where p.preco = ALL (select precoProduto from ItemPedido where produto = p)""";

        return Stream.of(arguments(jpql1), arguments(jpql2));
    }
}