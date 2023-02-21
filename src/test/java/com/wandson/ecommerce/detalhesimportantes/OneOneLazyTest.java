package com.wandson.ecommerce.detalhesimportantes;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Pedido;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class OneOneLazyTest extends EntityManagerTest {

    @Test
    void mostrarProblema() {
        System.out.println("BUSCANDO UM PEDIDO:");
        Pedido pedido = entityManager.find(Pedido.class, 1);
        Assertions.assertNotNull(pedido);

        System.out.println("----------------------------------------------------");

        System.out.println("BUSCANDO UMA LISTA DE PEDIDOS:");
        List<Pedido> lista = entityManager
                .createQuery("select p from Pedido p", Pedido.class)
                .getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }
}