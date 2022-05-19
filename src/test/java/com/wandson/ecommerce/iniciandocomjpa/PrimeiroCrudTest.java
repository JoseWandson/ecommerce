package com.wandson.ecommerce.iniciandocomjpa;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Cliente;
import com.wandson.ecommerce.model.Produto;
import com.wandson.ecommerce.model.SexoCliente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

class PrimeiroCrudTest extends EntityManagerTest {

    @Test
    void inserirRegistro() {
        var cliente = new Cliente();
        cliente.setNome("José Lucas");
        cliente.setSexo(SexoCliente.MASCULINO);
        cliente.setCpf("333");

        entityManager.getTransaction().begin();
        entityManager.persist(cliente);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Cliente clienteVerificacao = entityManager.find(Cliente.class, cliente.getId());
        Assertions.assertNotNull(clienteVerificacao);
    }

    @Test
    void buscarPorIdentificador() {
        Produto produto = entityManager.find(Produto.class, 1);

        Assertions.assertNotNull(produto);
        Assertions.assertEquals("Kindle", produto.getNome());
    }

    @Test
    void atualizarRegistro() {
        var cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("Fernando Medeiros Silva");
        cliente.setCpf("000");
        cliente.setSexo(SexoCliente.MASCULINO);

        entityManager.getTransaction().begin();
        entityManager.merge(cliente);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Cliente clienteVerificacao = entityManager.find(Cliente.class, cliente.getId());
        Assertions.assertEquals("Fernando Medeiros Silva", clienteVerificacao.getNome());
    }

    @Test
    void removerRegistro() {
        Cliente cliente = entityManager.find(Cliente.class, 2);

        entityManager.getTransaction().begin();
        if (Objects.nonNull(cliente.getPedidos()) && !cliente.getPedidos().isEmpty()) {
            cliente.getPedidos().forEach(pedido -> {
                pedido.getItens().forEach(entityManager::remove);
                entityManager.remove(pedido.getPagamento());
                entityManager.remove(pedido);
            });
        }
        entityManager.remove(cliente);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Cliente clienteVerificacao = entityManager.find(Cliente.class, cliente.getId());
        Assertions.assertNull(clienteVerificacao);
    }
}
