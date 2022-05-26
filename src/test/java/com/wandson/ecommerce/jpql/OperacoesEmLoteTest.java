package com.wandson.ecommerce.jpql;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Produto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;

class OperacoesEmLoteTest extends EntityManagerTest {

    private static final int LIMITE_INSERCOES = 4;

    @Test
    void inserirEmLote() {
        InputStream in = OperacoesEmLoteTest.class.getClassLoader()
                .getResourceAsStream("produtos/importar.txt");

        Assertions.assertNotNull(in);
        var reader = new BufferedReader(new InputStreamReader(in));

        entityManager.getTransaction().begin();

        var contadorInsercoes = 0;

        for (String linha : reader.lines().toList()) {
            if (linha.isBlank()) {
                continue;
            }

            String[] produtoColuna = linha.split(";");
            Produto produto = new Produto();
            produto.setNome(produtoColuna[0]);
            produto.setDescricao(produtoColuna[1]);
            produto.setPreco(new BigDecimal(produtoColuna[2]));
            produto.setDataCriacao(LocalDateTime.now());

            entityManager.persist(produto);

            if (++contadorInsercoes == LIMITE_INSERCOES) {
                entityManager.flush();
                entityManager.clear();

                contadorInsercoes = 0;

                System.out.println("---------------------------------");
            }
        }

        entityManager.getTransaction().commit();
    }
}