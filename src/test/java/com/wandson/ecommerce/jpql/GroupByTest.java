package com.wandson.ecommerce.jpql;

import com.wandson.ecommerce.EntityManagerTest;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class GroupByTest extends EntityManagerTest {

    @ParameterizedTest
    @MethodSource("getAgrupamentos")
    void agruparResultado(String jpql) {
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = typedQuery.getResultList();

        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @ParameterizedTest
    @MethodSource("getAgrupamentosEFiltros")
    void agruparEFiltrarResultado_totalDeVendasPorMes(String jpql) {
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

    private static Stream<Arguments> getAgrupamentos() {
//         Quantidade de produtos por categoria
        var jpql1 = "select c.nome, count(p.id) from Categoria c join c.produtos p group by c.id";

//         Total de vendas por mês
        var jpql2 = """
                select concat(year(p.dataCriacao), '/', function('monthname', p.dataCriacao)), sum(p.total)
                from Pedido p
                group by year (p.dataCriacao), month (p.dataCriacao)""";

//         Total de vendas por categoria
        var jpql3 = """
                select c.nome, sum(ip.precoProduto)
                from ItemPedido ip
                         join ip.produto pro
                         join pro.categorias c
                group by c.id""";

//         Total de vendas por cliente
        var jpql4 = """
                select c.nome, sum(ip.precoProduto)
                from ItemPedido ip
                         join ip.pedido p
                         join p.cliente c
                group by c.id""";

//         Total de vendas por dia e por categoria
        var jpql5 = """
                select concat(year(p.dataCriacao), '/', month(p.dataCriacao), '/', day(p.dataCriacao)),
                       concat(c.nome, ': ', sum(ip.precoProduto))
                from ItemPedido ip
                         join ip.pedido p
                         join ip.produto pro
                         join pro.categorias c
                group by year (p.dataCriacao), month (p.dataCriacao), day (p.dataCriacao), c.id
                order by p.dataCriacao, c.nome""";

        return Stream.of(arguments(jpql1), arguments(jpql2), arguments(jpql3), arguments(jpql4), arguments(jpql5));
    }

    private static Stream<Arguments> getAgrupamentosEFiltros() {
//         Total de vendas por Mês
        var jpql1 = """
                select concat(year(p.dataCriacao), '/', function('monthname', p.dataCriacao)), sum(p.total)
                from Pedido p
                where year (p.dataCriacao) = year (current_date)
                group by year (p.dataCriacao), month (p.dataCriacao)""";

//         Total de vendas por categoria
        var jpql2 = """
                select c.nome, sum(ip.precoProduto)
                from ItemPedido ip
                         join ip.produto pro
                         join pro.categorias c
                         join ip.pedido p
                where year (p.dataCriacao) = year (current_date) and month (p.dataCriacao) = month (current_date)
                group by c.id""";

//         Total de vendas por cliente
        var jpql3 = """
                select c.nome, sum(ip.precoProduto)
                from ItemPedido ip
                         join ip.pedido p
                         join p.cliente c
                         join ip.pedido p
                where year (p.dataCriacao) = year (current_date) and month (p.dataCriacao) >= (month (current_date) - 3)
                group by c.id""";

        return Stream.of(arguments(jpql1), arguments(jpql2), arguments(jpql3));
    }
}