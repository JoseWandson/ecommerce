package com.wandson.ecommerce;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class EntityManagerTest extends EntityManagerFactoryTest {

    protected EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    public void tearDown() {
        entityManager.close();
    }
}
