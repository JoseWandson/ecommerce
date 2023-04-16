package com.wandson.ecommerce.concorrencia;

import com.wandson.ecommerce.EntityManagerFactoryTest;
import com.wandson.ecommerce.model.Produto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LockOtimistaTest extends EntityManagerFactoryTest {

    @Test
    void usarLockOtimista() {
        Runnable runnable1 = () -> {
            EntityManager entityManager1 = entityManagerFactory.createEntityManager();
            entityManager1.getTransaction().begin();

            log("Runnable 01 vai carregar o produto 1.");
            Produto produto = entityManager1.find(Produto.class, 1);

            log("Runnable 01 vai esperar por 3 segundos.");
            esperar(3);

            log("Runnable 01 vai alterar o produto.");
            produto.setDescricao("Descrição detalhada.");

            log("Runnable 01 vai confirmar a transação.");
            entityManager1.getTransaction().commit();
            entityManager1.close();
        };

        Runnable runnable2 = () -> {
            EntityManager entityManager2 = entityManagerFactory.createEntityManager();
            entityManager2.getTransaction().begin();

            log("Runnable 02 vai carregar o produto 1.");
            Produto produto = entityManager2.find(Produto.class, 1);

            log("Runnable 02 vai esperar por 1 segundo.");
            esperar(1);

            log("Runnable 02 vai alterar o produto.");
            produto.setDescricao("Descrição massa!");

            log("Runnable 02 vai confirmar a transação.");
            entityManager2.getTransaction().commit();
            entityManager2.close();
        };

        var thread1 = new Thread(runnable1);
        var thread2 = new Thread(runnable2);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        EntityManager entityManager3 = entityManagerFactory.createEntityManager();
        Produto produto = entityManager3.find(Produto.class, 1);
        entityManager3.close();

        Assertions.assertEquals("Descrição massa!", produto.getDescricao());

        log("Encerrando método de teste.");
    }
}