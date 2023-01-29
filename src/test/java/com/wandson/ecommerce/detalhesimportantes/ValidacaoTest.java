package com.wandson.ecommerce.detalhesimportantes;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Cliente;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ValidacaoTest extends EntityManagerTest {

    @Test
    void validarCliente() {
        entityManager.getTransaction().begin();

        Cliente cliente = new Cliente();
        Assertions.assertThrows(ConstraintViolationException.class, () -> entityManager.merge(cliente));
    }
}