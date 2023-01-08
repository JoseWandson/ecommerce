package com.wandson.ecommerce.consultasnativas;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Cliente;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class StoredProceduresTest extends EntityManagerTest {

    @Test
    void usarParametrosInEOut() {
        StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("buscar_nome_produto");
        storedProcedureQuery.registerStoredProcedureParameter("produto_id", Integer.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("produto_nome", String.class, ParameterMode.OUT);
        storedProcedureQuery.setParameter("produto_id", 1);

        String nome = (String) storedProcedureQuery.getOutputParameterValue("produto_nome");

        Assertions.assertEquals("Kindle", nome);
    }

    @Test
    @SuppressWarnings("unchecked")
    void receberListaDaProcedure() {
        StoredProcedureQuery storedProcedureQuery = entityManager
                .createStoredProcedureQuery("compraram_acima_media", Cliente.class);
        storedProcedureQuery.registerStoredProcedureParameter("ano", Integer.class, ParameterMode.IN);
        storedProcedureQuery.setParameter("ano", 2023);

        List<Cliente> lista = storedProcedureQuery.getResultList();

        Assertions.assertFalse(lista.isEmpty());
    }
}
