package com.wandson.ecommerce.relacionamentos;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Pedido;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

class RemovendoEntidadesReferenciadasTest extends EntityManagerTest {

    @Test
    void removerEntidadeRelacionada() {
        Pedido pedido = entityManager.find(Pedido.class, 1);

        Assertions.assertFalse(pedido.getItens().isEmpty());

        entityManager.getTransaction().begin();
        pedido.getItens().forEach(entityManager::remove);
        if (Objects.nonNull(pedido.getPagamento())) {
            entityManager.remove(pedido.getPagamento());
        }
        entityManager.remove(pedido);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Pedido pedidoVerificacao = entityManager.find(Pedido.class, 1);
        Assertions.assertNull(pedidoVerificacao);
    }
}