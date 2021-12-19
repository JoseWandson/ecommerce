package com.wandson.ecommerce.listener;

import jakarta.persistence.PostLoad;
import lombok.extern.java.Log;

@Log
public class GenericoListener {

    @PostLoad
    public void logCarregamento(Object obj) {
        log.info("Entidade " + obj.getClass().getSimpleName() + " foi carregada.");
    }
}
