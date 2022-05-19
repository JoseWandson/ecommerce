package com.wandson.ecommerce.jpql;

import com.wandson.ecommerce.EntityManagerTest;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class GroupByTest extends EntityManagerTest {

    @Test
    void agruparResultado_quantidadeDeProdutospPorCategoria() {
        var jpql = "select c.nome, count(p.id) from Categoria c join c.produtos p group by c.id";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = typedQuery.getResultList();

        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    void agruparResultado_totalDeVendasPorMes() {
        var jpql = """
                select concat(year(p.dataCriacao), '/', function('monthname', p.dataCriacao)), sum(p.total)
                from Pedido p
                group by year (p.dataCriacao), month (p.dataCriacao)""";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = typedQuery.getResultList();

        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    void agruparResultado_totalDeVendasPorCategoria() {
        var jpql = """
                select c.nome, sum(ip.precoProduto)
                from ItemPedido ip
                         join ip.produto pro
                         join pro.categorias c
                group by c.id""";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = typedQuery.getResultList();

        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    void agruparResultado_totalDeVendasPorCliente() {
        var jpql = """
                select c.nome, sum(ip.precoProduto)
                from ItemPedido ip
                         join ip.pedido p
                         join p.cliente c
                group by c.id""";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = typedQuery.getResultList();

        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    void agruparResultado_totalDeVendasPorDiaEPorCategoria() {
        var jpql = """
                select concat(year(p.dataCriacao), '/', month(p.dataCriacao), '/', day(p.dataCriacao)),
                       concat(c.nome, ': ', sum(ip.precoProduto))
                from ItemPedido ip
                         join ip.pedido p
                         join ip.produto pro
                         join pro.categorias c
                group by year (p.dataCriacao), month (p.dataCriacao), day (p.dataCriacao), c.id
                order by p.dataCriacao, c.nome""";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = typedQuery.getResultList();

        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    void agruparEFiltrarResultado_totalDeVendasPorMes() {
        var jpql = """
                select concat(year(p.dataCriacao), '/', function('monthname', p.dataCriacao)), sum(p.total)
                from Pedido p
                where year (p.dataCriacao) = year (current_date)
                group by year (p.dataCriacao), month (p.dataCriacao)""";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = typedQuery.getResultList();

        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    void agruparEFiltrarResultado_totalDeVendasPorCategoria() {
        var jpql = """
                select c.nome, sum(ip.precoProduto)
                from ItemPedido ip
                         join ip.produto pro
                         join pro.categorias c
                         join ip.pedido p
                where year (p.dataCriacao) = year (current_date) and month (p.dataCriacao) = month (current_date)
                group by c.id""";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = typedQuery.getResultList();

        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    void agruparEFiltrarResultado_totalDeVendasPorCliente() {
        var jpql = """
                select c.nome, sum(ip.precoProduto)
                from ItemPedido ip
                         join ip.pedido p
                         join p.cliente c
                         join ip.pedido p
                where year (p.dataCriacao) = year (current_date) and month (p.dataCriacao) >= (month (current_date) - 3)
                group by c.id""";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = typedQuery.getResultList();

        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    void condicionarAgrupamentoComHaving() {
//         Total de vendas dentre as categorias que mais vendem.
        var jpql = """
                select cat.nome, sum(ip.precoProduto)
                from ItemPedido ip
                         join ip.produto pro
                         join pro.categorias cat
                group by cat.id
                having avg(ip.precoProduto) > 1500""";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = typedQuery.getResultList();

        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }
}