package com.wandson.ecommerce.mapeamentoavancado;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Cliente;
import com.wandson.ecommerce.model.SexoCliente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class SecondaryTableTest extends EntityManagerTest {

    @Test
    void salvarCliente() {
        Cliente cliente = new Cliente();
        cliente.setNome("Carlos Finotti");
        cliente.setSexo(SexoCliente.MASCULINO);
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));
        cliente.setCpf("555");

        entityManager.getTransaction().begin();
        entityManager.persist(cliente);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Cliente clienteVerificacao = entityManager.find(Cliente.class, cliente.getId());
        Assertions.assertNotNull(clienteVerificacao.getSexo());
    }
}