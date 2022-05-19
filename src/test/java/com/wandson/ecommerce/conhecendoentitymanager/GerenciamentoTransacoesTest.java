package com.wandson.ecommerce.conhecendoentitymanager;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Pedido;
import com.wandson.ecommerce.model.StatusPagamento;
import com.wandson.ecommerce.model.StatusPedido;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GerenciamentoTransacoesTest extends EntityManagerTest {

    @Test
    void abrirFecharCancelarTransacao() {
        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            try {
                entityManager.getTransaction().begin();
                metodoDeNegocio();
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                entityManager.getTransaction().rollback();
                throw e;
            }
        });

        Assertions.assertEquals("Pedido ainda não foi pago.", exception.getMessage());
    }

    private void metodoDeNegocio() {
        Pedido pedido = entityManager.find(Pedido.class, 2);
        pedido.setStatus(StatusPedido.PAGO);

        if (pedido.getPagamento() == null || !pedido.getPagamento().getStatus().equals(StatusPagamento.RECEBIDO)) {
            throw new RuntimeException("Pedido ainda não foi pago.");
        }
    }
}