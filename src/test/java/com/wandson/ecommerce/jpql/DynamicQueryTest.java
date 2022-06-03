package com.wandson.ecommerce.jpql;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Produto;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

class DynamicQueryTest extends EntityManagerTest {

    @Test
    void executarConsultaDinamica() {
        var consultado = new Produto();
        consultado.setNome("Câmera GoPro");

        List<Produto> lista = pesquisar(consultado);

        Assertions.assertFalse(lista.isEmpty());
        Assertions.assertEquals("Câmera GoPro Hero 7", lista.get(0).getNome());
    }

    private List<Produto> pesquisar(Produto consultado) {
        var jpql = new StringBuilder("select p from Produto p where 1 = 1");

        if (Objects.nonNull(consultado.getNome())) {
            jpql.append(" and p.nome like concat('%', :nome, '%')");
        }

        if (Objects.nonNull(consultado.getDescricao())) {
            jpql.append(" and p.descricao like concat('%', :descricao, '%')");
        }

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql.toString(), Produto.class);

        if (Objects.nonNull(consultado.getNome())) {
            typedQuery.setParameter("nome", consultado.getNome());
        }

        if (Objects.nonNull(consultado.getDescricao())) {
            typedQuery.setParameter("descricao", consultado.getDescricao());
        }

        return typedQuery.getResultList();
    }
}