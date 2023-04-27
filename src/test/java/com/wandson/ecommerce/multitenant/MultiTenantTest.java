package com.wandson.ecommerce.multitenant;

import com.wandson.ecommerce.EntityManagerFactoryTest;
import com.wandson.ecommerce.hibernate.EcmCurrentTenantIdentifierResolver;
import com.wandson.ecommerce.model.Produto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MultiTenantTest extends EntityManagerFactoryTest {

    @Test
    void usarAbordagemPorSchema() {
        EcmCurrentTenantIdentifierResolver.setTenantIdentifier("ecommerce");
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Produto produto = entityManager.find(Produto.class, 1);
            Assertions.assertEquals("Kindle", produto.getNome());
            EcmCurrentTenantIdentifierResolver.unload();
        }

        EcmCurrentTenantIdentifierResolver.setTenantIdentifier("loja_ecommerce");
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Produto produto = entityManager.find(Produto.class, 1);
            Assertions.assertEquals("Kindle Paperwhite", produto.getNome());
            EcmCurrentTenantIdentifierResolver.unload();
        }
    }

    @Test
    void usarAbordagemPorMaquina() {
        EcmCurrentTenantIdentifierResolver.setTenantIdentifier("ecommerce");
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Produto produto = entityManager.find(Produto.class, 1);
            Assertions.assertEquals("Kindle", produto.getNome());
            EcmCurrentTenantIdentifierResolver.unload();
        }

        EcmCurrentTenantIdentifierResolver.setTenantIdentifier("loja_ecommerce");
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Produto produto = entityManager.find(Produto.class, 1);
            Assertions.assertEquals("Kindle Paperwhite", produto.getNome());
            EcmCurrentTenantIdentifierResolver.unload();
        }
    }
}