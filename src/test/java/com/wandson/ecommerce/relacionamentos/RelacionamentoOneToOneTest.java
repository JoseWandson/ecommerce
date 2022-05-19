package com.wandson.ecommerce.relacionamentos;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.NotaFiscal;
import com.wandson.ecommerce.model.PagamentoCartao;
import com.wandson.ecommerce.model.Pedido;
import com.wandson.ecommerce.model.StatusPagamento;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Objects;

class RelacionamentoOneToOneTest extends EntityManagerTest {

    @Test
    void verificarRelacionamento() {
        Pedido pedido = entityManager.find(Pedido.class, 1);

        var pagamentoCartao = new PagamentoCartao();
        pagamentoCartao.setNumeroCartao("1234");
        pagamentoCartao.setStatus(StatusPagamento.PROCESSANDO);
        pagamentoCartao.setPedido(pedido);

        entityManager.getTransaction().begin();
        if (Objects.nonNull(pedido.getPagamento())) {
            entityManager.remove(pedido.getPagamento());
        }
        entityManager.persist(pagamentoCartao);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Pedido pedidoVerificacao = entityManager.find(Pedido.class, pedido.getId());
        Assertions.assertNotNull(pedidoVerificacao.getPagamento());
    }

    @Test
    void verificarRelacionamentoPedidoNotaFiscal() {
        Pedido pedido = entityManager.find(Pedido.class, 1);

        var notaFiscal = new NotaFiscal();
        notaFiscal.setXml("TESTE".getBytes());
        notaFiscal.setDataEmissao(LocalDateTime.now());
        notaFiscal.setPedido(pedido);

        entityManager.getTransaction().begin();
        entityManager.persist(notaFiscal);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Pedido pedidoVerificacao = entityManager.find(Pedido.class, pedido.getId());
        Assertions.assertNotNull(pedidoVerificacao.getNotaFiscal());
    }

}
