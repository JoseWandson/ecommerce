package com.wandson.ecommerce.jpql;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.NotaFiscal;
import com.wandson.ecommerce.model.Pedido;
import com.wandson.ecommerce.model.StatusPagamento;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class PassandoParametrosTest extends EntityManagerTest {

    @Test
    void passarParametroLocalDateTime() {
        String jpql = "select nf from NotaFiscal nf where nf.dataEmissao <= ?1";

        TypedQuery<NotaFiscal> typedQuery = entityManager.createQuery(jpql, NotaFiscal.class);
        typedQuery.setParameter(1, LocalDateTime.now());

        List<NotaFiscal> lista = typedQuery.getResultList();
        Assertions.assertEquals(1, lista.size());
    }

    @Test
    void passarParametro() {
        String jpql = "select p from Pedido p join p.pagamento pag where p.id = :pedidoId and pag.status = ?1";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        typedQuery.setParameter("pedidoId", 2);
        typedQuery.setParameter(1, StatusPagamento.PROCESSANDO);

        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertEquals(1, lista.size());
    }
}