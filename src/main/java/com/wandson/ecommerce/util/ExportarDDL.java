package com.wandson.ecommerce.util;

import java.util.HashMap;
import java.util.Map;

public class ExportarDDL {

    public static void main(String[] args) {
        Map<String, String> propriedades = new HashMap<>();

        propriedades.put("jakarta.persistence.schema-generation.scripts.action",
                "drop-and-create");
        propriedades.put("jakarta.persistence.schema-generation.scripts.create-target",
                "C:/tmp/sql/script-criacao-exportado.sql");
        propriedades.put("jakarta.persistence.schema-generation.scripts.drop-target",
                "C:/tmp/sql/script-remocao-exportado.sql");

        ExecutarDDL.executarPropriedades(propriedades);
    }
}