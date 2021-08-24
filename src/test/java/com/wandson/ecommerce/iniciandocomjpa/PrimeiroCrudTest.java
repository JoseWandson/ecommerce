package com.wandson.ecommerce.iniciandocomjpa;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Cliente;
import com.wandson.ecommerce.model.Produto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PrimeiroCrudTest extends EntityManagerTest {

    @Test
    void inserirRegistro() {
        Cliente cliente = new Cliente();
        cliente.setId(3);
        cliente.setNome("José Lucas");

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
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("Fernando Medeiros Silva");

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
        entityManager.remove(cliente);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Cliente clienteVerificacao = entityManager.find(Cliente.class, cliente.getId());
        Assertions.assertNull(clienteVerificacao);
    }
}
