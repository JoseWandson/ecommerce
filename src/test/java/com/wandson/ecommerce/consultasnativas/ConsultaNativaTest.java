package com.wandson.ecommerce.consultasnativas;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.ItemPedido;
import com.wandson.ecommerce.model.Produto;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@SuppressWarnings("unchecked")
class ConsultaNativaTest extends EntityManagerTest {

    @Test
    void executarSQL() {
        String sql = "select id, nome from produto";
        Query query = entityManager.createNativeQuery(sql);

        List<Object[]> lista = query.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.printf("Produto => ID: %s, Nome: %s%n", arr[0], arr[1]));
    }

    @Test
    void passarParametros() {
        var sql = """
                SELECT
                    prd_id id,
                    prd_nome nome,
                    prd_descricao descricao,
                    prd_data_criacao data_criacao,
                    prd_data_ultima_atualizacao data_ultima_atualizacao,
                    prd_preco preco,
                    prd_foto foto
                FROM
                    ecm_produto where prd_id = :id""";
        Query query = entityManager.createNativeQuery(sql, Produto.class);
        query.setParameter("id", 201);

        List<Produto> lista = query.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.printf("Produto => ID: %s, Nome: %s%n", obj.getId(), obj.getNome()));
    }

    @ParameterizedTest
    @MethodSource("getSqls")
    void executarSQLRetornandoEntidade(String sql) {
        Query query = entityManager.createNativeQuery(sql, Produto.class);

        List<Produto> lista = query.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.printf("Produto => ID: %s, Nome: %s%n", obj.getId(), obj.getNome()));
    }

    @Test
    void usarSQLResultSetMapping01() {
        var sql = """
                SELECT
                    id,
                    nome,
                    descricao,
                    data_criacao,
                    data_ultima_atualizacao,
                    preco,
                    foto
                FROM
                    produto_loja""";

        Query query = entityManager.createNativeQuery(sql, "produto_loja.Produto");

        List<Produto> lista = query.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.printf("Produto => ID: %s, Nome: %s%n", obj.getId(), obj.getNome()));
    }

    @Test
    void usarSQLResultSetMapping02() {
        var sql = """
                SELECT
                    ip.*, p.*
                FROM
                    item_pedido ip join produto p on p.id = ip.produto_id""";

        Query query = entityManager.createNativeQuery(sql, "item_pedido-produto.ItemPedido-Produto");

        List<Object[]> lista = query.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.printf("Pedido => ID: %s --- Produto => ID: %s, Nome: %s%n",
                ((ItemPedido) arr[0]).getId().getPedidoId(), ((Produto) arr[1]).getId(), ((Produto) arr[1]).getNome()));
    }

    private static Stream<Arguments> getSqls() {
//        Produtos da tabela produto
        var sql1 = """
                SELECT
                    id,
                    nome,
                    descricao,
                    data_criacao,
                    data_ultima_atualizacao,
                    preco,
                    foto
                FROM
                    produto""";

//         Produtos da tabela produto_loja
        var sql2 = """
                SELECT
                    id,
                    nome,
                    descricao,
                    data_criacao,
                    data_ultima_atualizacao,
                    preco,
                    foto
                FROM
                    produto_loja""";

//         Produtos da tabela ecm_produto
        var sql3 = """
                SELECT
                    prd_id id,
                    prd_nome nome,
                    prd_descricao descricao,
                    prd_data_criacao data_criacao,
                    prd_data_ultima_atualizacao data_ultima_atualizacao,
                    prd_preco preco,
                    prd_foto foto
                FROM
                    ecm_produto""";

//         Produtos da tabela erp_produto
        var sql4 = """
                SELECT
                    id,
                    nome,
                    descricao,
                    NULL data_criacao,
                    NULL data_ultima_atualizacao,
                    preco,
                    NULL foto
                FROM
                    erp_produto""";

        return Stream.of(arguments(sql1), arguments(sql2), arguments(sql3), arguments(sql4));
    }
}
