package com.wandson.ecommerce.service;

import com.wandson.ecommerce.model.Pedido;
import lombok.extern.java.Log;

@Log
public class NotaFiscalService {

    public void gerar(Pedido pedido) {
        log.info("Gerando nota para o pedido " + pedido.getId() + ".");
    }
}
