package com.wandson.ecommerce.detalhesimportantes;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Produto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class ConversorTest extends EntityManagerTest {

    @Test
    void converter() {
        var produto = new Produto();
        produto.setDataCriacao(LocalDateTime.now());
        produto.setNome("Carregador de Notebook Dell");
        produto.setAtivo(Boolean.TRUE);

        entityManager.getTransaction().begin();

        entityManager.persist(produto);

        entityManager.getTransaction().commit();

        entityManager.clear();

        var produtoVerificacao = entityManager.find(Produto.class, produto.getId());
        Assertions.assertTrue(produtoVerificacao.getAtivo());
    }
}