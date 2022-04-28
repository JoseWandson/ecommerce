package com.wandson.ecommerce.jpql;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Pedido;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BasicoJPQLTest extends EntityManagerTest {

    @Test
    void buscarPorIdentificador() {
        // entityManager.find(Pedido.class, 1)

        TypedQuery<Pedido> typedQuery = entityManager
                .createQuery("select p from Pedido p where p.id = 1", Pedido.class);

        Pedido pedido = typedQuery.getSingleResult();
        Assertions.assertNotNull(pedido);
    }
}