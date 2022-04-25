package com.wandson.ecommerce.operacoesemcascata;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Categoria;
import com.wandson.ecommerce.model.Cliente;
import com.wandson.ecommerce.model.ItemPedido;
import com.wandson.ecommerce.model.ItemPedidoId;
import com.wandson.ecommerce.model.Pedido;
import com.wandson.ecommerce.model.Produto;
import com.wandson.ecommerce.model.StatusPedido;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

class CascadeTypeMergeTest extends EntityManagerTest {

    @Test
    @Disabled("Precisa do CascadeType.MERGE para o teste funcionar.")
    void atualizarPedidoComItens() {
        Cliente cliente = entityManager.find(Cliente.class, 1);
        Produto produto = entityManager.find(Produto.class, 1);

        Pedido pedido = new Pedido();
        pedido.setId(1);
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.AGUARDANDO);

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(new ItemPedidoId());
        itemPedido.getId().setPedidoId(pedido.getId());
        itemPedido.getId().setProdutoId(produto.getId());
        itemPedido.setPedido(pedido);
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(3);
        itemPedido.setPrecoProduto(produto.getPreco());

        pedido.setItens(List.of(itemPedido)); // CascadeType.MERGE

        entityManager.getTransaction().begin();
        entityManager.merge(pedido);
        entityManager.getTransaction().commit();

        entityManager.clear();

        ItemPedido itemPedidoVerificacao = entityManager.find(ItemPedido.class, itemPedido.getId());
        Assertions.assertEquals(3, itemPedidoVerificacao.getQuantidade());
    }

    @Test
    @Disabled("Precisa do CascadeType.MERGE para o teste funcionar.")
    void atualizarItemPedidoComPedido() {
        Cliente cliente = entityManager.find(Cliente.class, 1);
        Produto produto = entityManager.find(Produto.class, 1);

        Pedido pedido = new Pedido();
        pedido.setId(1);
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.PAGO);

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(new ItemPedidoId());
        itemPedido.getId().setPedidoId(pedido.getId());
        itemPedido.getId().setProdutoId(produto.getId());
        itemPedido.setPedido(pedido); // CascadeType.MERGE
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(5);
        itemPedido.setPrecoProduto(produto.getPreco());

        pedido.setItens(List.of(itemPedido));

        entityManager.getTransaction().begin();
        entityManager.merge(itemPedido);
        entityManager.getTransaction().commit();

        entityManager.clear();

        ItemPedido itemPedidoVerificacao = entityManager.find(ItemPedido.class, itemPedido.getId());
        Assertions.assertEquals(StatusPedido.PAGO, itemPedidoVerificacao.getPedido().getStatus());
    }

    @Test
    @Disabled("Precisa do CascadeType.MERGE para o teste funcionar.")
    void atualizarProdutoComCategoria() {
        Produto produto = new Produto();
        produto.setId(1);
        produto.setDataUltimaAtualizacao(LocalDateTime.now());
        produto.setPreco(new BigDecimal(500));
        produto.setNome("Kindle");
        produto.setDescricao("Agora com iluminação embutida ajustável.");

        Categoria categoria = new Categoria();
        categoria.setId(2);
        categoria.setNome("Tablets");

        produto.setCategorias(List.of(categoria)); // CascadeType.MERGE

        entityManager.getTransaction().begin();
        entityManager.merge(produto);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Categoria categoriaVerificacao = entityManager.find(Categoria.class, categoria.getId());
        Assertions.assertEquals("Tablets", categoriaVerificacao.getNome());
    }
}