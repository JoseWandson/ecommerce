package com.wandson.ecommerce.cache;

import com.wandson.ecommerce.EntityManagerFactoryTest;
import com.wandson.ecommerce.model.Pedido;
import jakarta.persistence.Cache;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class CacheTest extends EntityManagerFactoryTest {

    @Test
    void buscarDoCache() {
        Assertions.assertDoesNotThrow(() -> {
            EntityManager entityManager1 = entityManagerFactory.createEntityManager();
            EntityManager entityManager2 = entityManagerFactory.createEntityManager();

            System.out.println("Buscando a partir da instância 1:");
            entityManager1.find(Pedido.class, 1);

            System.out.println("Buscando a partir da instância 2:");
            entityManager2.find(Pedido.class, 1);

            entityManager1.close();
            entityManager2.close();
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

            entityManager1.close();
            entityManager2.close();
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

            entityManager1.close();
            entityManager2.close();
        });
    }

    @Test
    void verificarSeEstaNoCache() {
        Cache cache = entityManagerFactory.getCache();

        EntityManager entityManager1 = entityManagerFactory.createEntityManager();

        System.out.println("Buscando a partir da instância 1:");
        entityManager1.createQuery("select p from Pedido p", Pedido.class).getResultList();

        Assertions.assertTrue(cache.contains(Pedido.class, 1));
        Assertions.assertTrue(cache.contains(Pedido.class, 2));

        entityManager1.close();
    }

    @Test
    void analisarOpcoesCache() {
        Cache cache = entityManagerFactory.getCache();

        EntityManager entityManager1 = entityManagerFactory.createEntityManager();

        System.out.println("Buscando a partir da instância 1:");
        entityManager1.createQuery("select p from Pedido p", Pedido.class).getResultList();

        Assertions.assertTrue(cache.contains(Pedido.class, 1));

        entityManager1.close();
    }

    @Test
    void controlarCacheDinamicamente() {
        Assertions.assertDoesNotThrow(() -> {
            System.out.println("Buscando todos os pedidos..........................");
            EntityManager entityManager1 = entityManagerFactory.createEntityManager();
            entityManager1.setProperty("jakarta.persistence.cache.storeMode", CacheStoreMode.BYPASS);
            entityManager1
                    .createQuery("select p from Pedido p", Pedido.class)
                    .setHint("jakarta.persistence.cache.storeMode", CacheStoreMode.USE)
                    .getResultList();

            System.out.println("Buscando o pedido de ID igual a 2..................");
            EntityManager entityManager2 = entityManagerFactory.createEntityManager();
            Map<String, Object> propriedades = new HashMap<>();
            propriedades.put("jakarta.persistence.cache.storeMode", CacheStoreMode.BYPASS);
            propriedades.put("jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            entityManager2.find(Pedido.class, 2, propriedades);

            System.out.println("Buscando todos os pedidos (de novo)..........................");
            EntityManager entityManager3 = entityManagerFactory.createEntityManager();
            entityManager3
                    .createQuery("select p from Pedido p", Pedido.class)
                    .setHint("jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS)
                    .getResultList();

            entityManager1.close();
            entityManager2.close();
            entityManager3.close();
        });
    }

    @Test
    void ehcache() {
        Cache cache = entityManagerFactory.getCache();

        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();

        log("Buscando e incluindo no cache...");
        entityManager1
                .createQuery("select p from Pedido p", Pedido.class)
                .getResultList();
        log("---");

        esperar(1);
        Assertions.assertTrue(cache.contains(Pedido.class, 2));
        entityManager2.find(Pedido.class, 2);

        esperar(3);
        Assertions.assertFalse(cache.contains(Pedido.class, 2));

        entityManager1.close();
        entityManager2.close();
    }
}