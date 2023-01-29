package com.wandson.ecommerce.consultasnativas;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Cliente;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ViewTest extends EntityManagerTest {

    @Test
    @SuppressWarnings("unchecked")
    void executarView() {
        Query query = entityManager.createNativeQuery(
                """
                        select cli.id, cli.nome, sum(ped.total)
                        from pedido ped
                        join view_clientes_acima_media cli on cli.id = ped.cliente_id
                        group by ped.cliente_id""");

        List<Object[]> lista = query.getResultList();

        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.printf("Cliente => ID: %s, Nome: %s, Total: %s%n", arr));
    }

    @Test
    @SuppressWarnings("unchecked")
    void executarViewRetornandoCliente() {
        Query query = entityManager.createNativeQuery(
                "select * from view_clientes_acima_media", Cliente.class);

        List<Cliente> lista = query.getResultList();

        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(obj -> System.out.printf("Cliente => ID: %s, Nome: %s%n", obj.getId(), obj.getNome()));
    }
}