package com.wandson.ecommerce.mapeamentobasico;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Categoria;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EstrategiaChavePrimariaTest extends EntityManagerTest {

    @Test
    void testarEstrategiaChave() {
        Categoria categoria = new Categoria();
        categoria.setNome("Natação");

        entityManager.getTransaction().begin();
        entityManager.persist(categoria);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Categoria categoriaVerificacao = entityManager.find(Categoria.class, categoria.getId());
        Assertions.assertNotNull(categoriaVerificacao);
    }
}
