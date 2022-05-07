package com.wandson.ecommerce.jpql;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Cliente;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ExpressoesCondicionaisTest extends EntityManagerTest {

    @Test
    void usarExpressaoCondicionalLike() {
        String jpql = "select c from Cliente c where c.nome like concat('%', :nome, '%')";

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(jpql, Cliente.class);
        typedQuery.setParameter("nome", "a");

        List<Cliente> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }
}