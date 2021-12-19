package com.wandson.ecommerce.listener;

import com.wandson.ecommerce.model.Pedido;
import com.wandson.ecommerce.service.NotaFiscalService;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.util.Objects;

public class GerarNotaFiscalListener {

    private final NotaFiscalService notaFiscalService = new NotaFiscalService();

    @PreUpdate
    @PrePersist
    public void gerar(Pedido pedido) {
        if (pedido.isPago() && Objects.isNull(pedido.getNotaFiscal())) {
            notaFiscalService.gerar(pedido);
        }
    }
}
