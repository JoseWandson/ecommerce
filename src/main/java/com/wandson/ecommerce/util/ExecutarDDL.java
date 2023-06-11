package com.wandson.ecommerce.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class ExecutarDDL {

    public static void main(String[] args) {
        Map<String, String> propriedades = new HashMap<>();

        propriedades.put("jakarta.persistence.jdbc.url",
                "jdbc:postgresql://localhost/ecommerce");

        propriedades.put("jakarta.persistence.schema-generation.database.action",
                "drop-and-create");

        executarPropriedades(propriedades);
    }

    static void executarPropriedades(Map<String, String> propriedades) {
        propriedades.put("jakarta.persistence.schema-generation.create-source",
                "metadata-then-script");
        propriedades.put("jakarta.persistence.schema-generation.drop-source",
                "metadata-then-script");

        propriedades.put("jakarta.persistence.schema-generation.create-script-source",
                "META-INF/banco-de-dados/script-criacao.sql");
        propriedades.put("jakarta.persistence.schema-generation.drop-script-source",
                "META-INF/banco-de-dados/script-remocao.sql");

        propriedades.put("jakarta.persistence.sql-load-script-source",
                "META-INF/banco-de-dados/dados-iniciais.sql");

        EntityManagerFactory entityManagerFactory = Persistence
                .createEntityManagerFactory("Ecommerce-PU", propriedades);

        entityManagerFactory.close();
    }
}