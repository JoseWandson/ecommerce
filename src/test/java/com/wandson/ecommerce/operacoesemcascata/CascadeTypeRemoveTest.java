package com.wandson.ecommerce.operacoesemcascata;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.ItemPedido;
import com.wandson.ecommerce.model.ItemPedidoId;
import com.wandson.ecommerce.model.Pedido;
import com.wandson.ecommerce.model.Produto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class CascadeTypeRemoveTest extends EntityManagerTest {

    @Test
    @Disabled("Precisa do CascadeType.REMOVE para o teste funcionar.")
    void removerPedidoEItens() {
        Pedido pedido = entityManager.find(Pedido.class, 1);

        entityManager.getTransaction().begin();
        entityManager.remove(pedido); // Necessário CascadeType.REMOVE no atributo "itens".
        entityManager.getTransaction().commit();

        entityManager.clear();

        Pedido pedidoVerificacao = entityManager.find(Pedido.class, pedido.getId());
        Assertions.assertNull(pedidoVerificacao);
    }

    @Test
    @Disabled("Precisa do CascadeType.REMOVE para o teste funcionar.")
    void removerItemPedidoEPedido() {
        ItemPedido itemPedido = entityManager.find(
                ItemPedido.class, new ItemPedidoId(1, 1));

        entityManager.getTransaction().begin();
        entityManager.remove(itemPedido); // Necessário CascadeType.REMOVE no atributo "pedido".
        entityManager.getTransaction().commit();

        entityManager.clear();

        Pedido pedidoVerificacao = entityManager.find(Pedido.class, itemPedido.getPedido().getId());
        Assertions.assertNull(pedidoVerificacao);
    }

    @Test
    void removerRelacaoProdutoCategoria() {
        Produto produto = entityManager.find(Produto.class, 1);

        Assertions.assertFalse(produto.getCategorias().isEmpty());

        entityManager.getTransaction().begin();
        produto.getCategorias().clear();
        entityManager.getTransaction().commit();

        entityManager.clear();

        Produto produtoVerificacao = entityManager.find(Produto.class, produto.getId());
        Assertions.assertTrue(produtoVerificacao.getCategorias().isEmpty());
    }

    @Test
    @Disabled("Precisa do orphanRemoval = true e CascadeType.PERSIST para o teste funcionar.")
    void removerItensOrfaos() {
        Pedido pedido = entityManager.find(Pedido.class, 1);

        Assertions.assertFalse(pedido.getItens().isEmpty());

        entityManager.getTransaction().begin();
        pedido.getItens().clear();
        entityManager.getTransaction().commit();

        entityManager.clear();

        Pedido pedidoVerificacao = entityManager.find(Pedido.class, pedido.getId());
        Assertions.assertTrue(pedidoVerificacao.getItens().isEmpty());
    }
}