package com.wandson.ecommerce.iniciandocomjpa;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Produto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class OperacoesComTransacaoTest extends EntityManagerTest {

    @Test
    void inserirOPrimeiroObjeto(){
        Produto produto = new Produto();
        produto.setId(2);
        produto.setNome("Câmera Canon");
        produto.setDescricao("A melhor definição para suas fotos.");
        produto.setPreco(new BigDecimal(5000));

        entityManager.getTransaction().begin();
        entityManager.persist(produto);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Produto produtoVerificado = entityManager.find(Produto.class, produto.getId());
        Assertions.assertNotNull(produtoVerificado);
    }
}
