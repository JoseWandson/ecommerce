package com.wandson.ecommerce.jpql;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Pedido;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class PathExpressionTest extends EntityManagerTest {

    @Test
    void usarPathExpressions() {
        var jpql = "select p.cliente.nome from Pedido p";

        TypedQuery<String> typedQuery = entityManager.createQuery(jpql, String.class);

        List<String> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void buscarPedidosComProdutoEspecifico() {
        var jpql = "select p from Pedido p join p.itens i where i.produto.id = 1";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);

        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertEquals(5, lista.size());
    }
}