package com.wandson.ecommerce.conhecendoentitymanager;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Pedido;
import com.wandson.ecommerce.model.StatusPedido;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FlushTest extends EntityManagerTest {

    @Test
    void chamarFlush() {
        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            try {
                entityManager.getTransaction().begin();

                Pedido pedido = entityManager.find(Pedido.class, 1);
                pedido.setStatus(StatusPedido.PAGO);

                entityManager.flush();

                if (pedido.getPagamento() == null) {
                    throw new RuntimeException("Pedido ainda não foi pago.");
                }

                entityManager.getTransaction().commit();
            } catch (Exception e) {
                entityManager.getTransaction().rollback();
                throw e;
            }
        });

        Assertions.assertEquals("Pedido ainda não foi pago.", exception.getMessage());
    }
}