package com.wandson.ecommerce.mapeamentoavancado;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Cliente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PropriedadesTransientesTest extends EntityManagerTest {

    @Test
    void validarPrimeiroNome() {
        Cliente cliente = entityManager.find(Cliente.class, 1);

        Assertions.assertEquals("Fernando", cliente.getPrimeiroNome());
    }
}
