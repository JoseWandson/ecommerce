package com.wandson.ecommerce.cache;

import com.wandson.ecommerce.model.Pedido;
import jakarta.persistence.Cache;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CacheTest {

    private static EntityManagerFactory entityManagerFactory;

    @BeforeAll
    public static void setUpBeforeClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory("Ecommerce-PU");
    }

    @Test
    void buscarDoCache() {
        Assertions.assertDoesNotThrow(() -> {
            EntityManager entityManager1 = entityManagerFactory.createEntityManager();
            EntityManager entityManager2 = entityManagerFactory.createEntityManager();

            System.out.println("Buscando a partir da instância 1:");
            entityManager1.find(Pedido.class, 1);

            System.out.println("Buscando a partir da instância 2:");
            entityManager2.find(Pedido.class, 1);
        });
    }

    @Test
    void adicionarPedidosNoCache() {
        Assertions.assertDoesNotThrow(() -> {
            EntityManager entityManager1 = entityManagerFactory.createEntityManager();
            EntityManager entityManager2 = entityManagerFactory.createEntityManager();

            System.out.println("Buscando a partir da instância 1:");
            entityManager1.createQuery("select p from Pedido p", Pedido.class).getResultList();

            System.out.println("Buscando a partir da instância 2:");
            entityManager2.find(Pedido.class, 1);
        });
    }

    @Test
    void removerDoCache() {
        Assertions.assertDoesNotThrow(() -> {
            Cache cache = entityManagerFactory.getCache();

            EntityManager entityManager1 = entityManagerFactory.createEntityManager();
            EntityManager entityManager2 = entityManagerFactory.createEntityManager();

            System.out.println("Buscando a partir da instância 1:");
            entityManager1.createQuery("select p from Pedido p", Pedido.class).getResultList();

            System.out.println("Removendo do cache");
            cache.evictAll();

            System.out.println("Buscando a partir da instância 2:");
            entityManager2.find(Pedido.class, 1);
            entityManager2.find(Pedido.class, 2);
        });
    }


    @AfterAll
    public static void tearDownAfterClass() {
        entityManagerFactory.close();
    }
}