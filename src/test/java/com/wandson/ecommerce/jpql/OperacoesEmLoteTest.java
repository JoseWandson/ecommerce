package com.wandson.ecommerce.jpql;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Produto;
import jakarta.persistence.Query;
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
        Assertions.assertDoesNotThrow(() -> {
            try (InputStream in = OperacoesEmLoteTest.class.getClassLoader()
                    .getResourceAsStream("produtos/importar.txt")) {
                Assertions.assertNotNull(in);
                try (var reader = new BufferedReader(new InputStreamReader(in))) {
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
        });
    }

    @Test
    void atualizarEmLote() {
        Assertions.assertDoesNotThrow(() -> {
            entityManager.getTransaction().begin();

            var jpql = """
                    update Produto p
                    set p.preco = p.preco + (p.preco * 0.1)
                    where exists(select 1 from p.categorias c where c.id = :categoria)""";

            Query query = entityManager.createQuery(jpql);
            query.setParameter("categoria", 2);
            query.executeUpdate();

            entityManager.getTransaction().commit();
        });
    }

    @Test
    void removerEmLote() {
        Assertions.assertDoesNotThrow(() -> {
            entityManager.getTransaction().begin();

            var jpql = "delete from Produto p where p.id between 8 and 12";

            Query query = entityManager.createQuery(jpql);
            query.executeUpdate();

            entityManager.getTransaction().commit();
        });
    }
}