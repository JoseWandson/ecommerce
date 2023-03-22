package com.wandson.ecommerce.concorrencia;

import com.wandson.ecommerce.model.Produto;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LockPessimistaTest {

    private static EntityManagerFactory entityManagerFactory;

    @BeforeAll
    public static void setUpBeforeAll() {
        entityManagerFactory = Persistence.createEntityManagerFactory("Ecommerce-PU");
    }

    @Test
    void usarLockPessimistaLockModeTypePessimisticRead() {
        Runnable runnable1 = () -> {
            log("Iniciando Runnable 01.");

            var novaDescricao = "Descrição detalhada. CTM: " + System.currentTimeMillis();

            var entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            log("Runnable 01 vai carregar o produto 1.");
            var produto = entityManager.find(Produto.class, 1, LockModeType.PESSIMISTIC_READ);

            log("Runnable 01 vai alterar o produto.");
            produto.setDescricao(novaDescricao);

            log("Runnable 01 vai esperar por 3 segundo(s).");
            esperar(3);

            log("Runnable 01 vai confirmar a transação.");
            entityManager.getTransaction().commit();
            entityManager.close();

            log("Encerrando Runnable 01.");
        };

        Runnable runnable2 = () -> {
            log("Iniciando Runnable 02.");

            var novaDescricao = "Descrição massa! CTM: " + System.currentTimeMillis();

            var entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            log("Runnable 02 vai carregar o produto 2.");
            var produto = entityManager.find(Produto.class, 1, LockModeType.PESSIMISTIC_READ);

            log("Runnable 02 vai alterar o produto.");
            produto.setDescricao(novaDescricao);

            log("Runnable 02 vai esperar por 1 segundo(s).");
            esperar(1);

            log("Runnable 02 vai confirmar a transação.");
            entityManager.getTransaction().commit();
            entityManager.close();

            log("Encerrando Runnable 02.");
        };

        var thread1 = new Thread(runnable1);
        var thread2 = new Thread(runnable2);

        thread1.start();

        esperar(1);
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        var entityManager = entityManagerFactory.createEntityManager();
        var produto = entityManager.find(Produto.class, 1);
        entityManager.close();

        Assertions.assertTrue(produto.getDescricao().startsWith("Descrição massa!"));

        log("Encerrando método de teste.");
    }

    @Test
    void usarLockPessimistaLockModeTypePessimisticWrite() {
        Runnable runnable1 = () -> {
            log("Iniciando Runnable 01.");

            var novaDescricao = "Descrição detalhada. CTM: " + System.currentTimeMillis();

            var entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            log("Runnable 01 vai carregar o produto 1.");
            var produto = entityManager.find(Produto.class, 1, LockModeType.PESSIMISTIC_WRITE);

            log("Runnable 01 vai alterar o produto.");
            produto.setDescricao(novaDescricao);

            log("Runnable 01 vai esperar por 3 segundo(s).");
            esperar(3);

            log("Runnable 01 vai confirmar a transação.");
            entityManager.getTransaction().commit();
            entityManager.close();

            log("Encerrando Runnable 01.");
        };

        Runnable runnable2 = () -> {
            log("Iniciando Runnable 02.");

            var novaDescricao = "Descrição massa! CTM: " + System.currentTimeMillis();

            var entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            log("Runnable 02 vai carregar o produto 2.");
            Produto produto = entityManager.find(Produto.class, 1, LockModeType.PESSIMISTIC_WRITE);

            log("Runnable 02 vai alterar o produto.");
            produto.setDescricao(novaDescricao);

            log("Runnable 02 vai esperar por 1 segundo(s).");
            esperar(1);

            log("Runnable 02 vai confirmar a transação.");
            entityManager.getTransaction().commit();
            entityManager.close();

            log("Encerrando Runnable 02.");
        };

        var thread1 = new Thread(runnable1);
        var thread2 = new Thread(runnable2);

        thread1.start();

        esperar(1);
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        var entityManager = entityManagerFactory.createEntityManager();
        Produto produto = entityManager.find(Produto.class, 1);
        entityManager.close();

        Assertions.assertTrue(produto.getDescricao().startsWith("Descrição massa!"));

        log("Encerrando método de teste.");
    }


    @AfterAll
    public static void tearDownAfterAll() {
        entityManagerFactory.close();
    }

    private static void log(Object obj, Object... args) {
        System.out.printf("[LOG " + System.currentTimeMillis() + "] " + obj + "%n", args);
    }

    private static void esperar(int segundos) {
        try {
            Thread.sleep(segundos * 1000L);
        } catch (InterruptedException ignored) {
        }
    }
}