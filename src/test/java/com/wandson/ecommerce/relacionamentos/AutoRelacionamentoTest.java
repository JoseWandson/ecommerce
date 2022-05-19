package com.wandson.ecommerce.relacionamentos;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Categoria;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AutoRelacionamentoTest extends EntityManagerTest {

    @Test
    void verificarRelacionamento() {
        var categoriaPai = new Categoria();
        categoriaPai.setNome("Basquete");

        var categoria = new Categoria();
        categoria.setNome("Uniformes");
        categoria.setCategoriaPai(categoriaPai);

        entityManager.getTransaction().begin();
        entityManager.persist(categoriaPai);
        entityManager.persist(categoria);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Categoria categoriaVerificacao = entityManager.find(Categoria.class, categoria.getId());
        Assertions.assertNotNull(categoriaVerificacao.getCategoriaPai());

        Categoria categoriaPaiVerificacao = entityManager.find(Categoria.class, categoriaPai.getId());
        Assertions.assertFalse(categoriaPaiVerificacao.getCategorias().isEmpty());
    }

}
