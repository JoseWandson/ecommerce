package com.wandson.ecommerce.mapeamentoavancado;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Cliente;
import com.wandson.ecommerce.model.ItemPedido;
import com.wandson.ecommerce.model.ItemPedidoId;
import com.wandson.ecommerce.model.Pedido;
import com.wandson.ecommerce.model.Produto;
import com.wandson.ecommerce.model.StatusPedido;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ChaveCompostaTest extends EntityManagerTest {

    @Test
    void salvarItem() {
        entityManager.getTransaction().begin();

        Cliente cliente = entityManager.find(Cliente.class, 1);
        Produto produto = entityManager.find(Produto.class, 1);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.AGUARDANDO);
        pedido.setTotal(produto.getPreco());

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(new ItemPedidoId());
        itemPedido.setPedido(pedido);
        itemPedido.setProduto(produto);
        itemPedido.setPrecoProduto(produto.getPreco());
        itemPedido.setQuantidade(1);

        entityManager.persist(pedido);
        entityManager.persist(itemPedido);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Pedido pedidoVerificacao = entityManager.find(Pedido.class, pedido.getId());
        Assertions.assertNotNull(pedidoVerificacao);
        Assertions.assertFalse(pedidoVerificacao.getItens().isEmpty());
    }

    @Test
    void bucarItem() {
        ItemPedido itemPedido = entityManager.find(
                ItemPedido.class, new ItemPedidoId(1, 1));

        Assertions.assertNotNull(itemPedido);
    }
}