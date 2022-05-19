package com.wandson.ecommerce.mapeamentoavancado;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Cliente;
import com.wandson.ecommerce.model.Pagamento;
import com.wandson.ecommerce.model.PagamentoCartao;
import com.wandson.ecommerce.model.Pedido;
import com.wandson.ecommerce.model.SexoCliente;
import com.wandson.ecommerce.model.StatusPagamento;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

class HerancaTest extends EntityManagerTest {

    @Test
    void salvarCliente() {
        var cliente = new Cliente();
        cliente.setNome("Fernanda Morais");
        cliente.setSexo(SexoCliente.FEMININO);
        cliente.setCpf("333");

        entityManager.getTransaction().begin();
        entityManager.persist(cliente);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Cliente clienteVerificacao = entityManager.find(Cliente.class, cliente.getId());
        Assertions.assertNotNull(clienteVerificacao.getId());
    }

    @Test
    void buscarPagamentos() {
        List<Pagamento> pagamentos = entityManager
                .createQuery("select p from Pagamento p", Pagamento.class)
                .getResultList();

        Assertions.assertFalse(pagamentos.isEmpty());
    }

    @Test
    void incluirPagamentoPedido() {
        Pedido pedido = entityManager.find(Pedido.class, 1);

        var pagamentoCartao = new PagamentoCartao();
        pagamentoCartao.setPedido(pedido);
        pagamentoCartao.setStatus(StatusPagamento.PROCESSANDO);
        pagamentoCartao.setNumeroCartao("123");

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
}