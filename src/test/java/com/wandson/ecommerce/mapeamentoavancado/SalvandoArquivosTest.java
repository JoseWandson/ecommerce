package com.wandson.ecommerce.mapeamentoavancado;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.NotaFiscal;
import com.wandson.ecommerce.model.Pedido;
import com.wandson.ecommerce.model.Produto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

class SalvandoArquivosTest extends EntityManagerTest {

    @Test
    void salvarXmlNota() {
        Pedido pedido = entityManager.find(Pedido.class, 1);

        NotaFiscal notaFiscal = new NotaFiscal();
        notaFiscal.setPedido(pedido);
        notaFiscal.setDataEmissao(new Date());
        notaFiscal.setXml(carregarNotaFiscal());

        entityManager.getTransaction().begin();
        entityManager.persist(notaFiscal);
        entityManager.getTransaction().commit();

        entityManager.clear();

        NotaFiscal notaFiscalVerificacao = entityManager.find(NotaFiscal.class, notaFiscal.getId());
        Assertions.assertNotNull(notaFiscalVerificacao.getXml());
        Assertions.assertTrue(notaFiscalVerificacao.getXml().length > 0);
    }

    @Test
    void salvarFotoProduto() {
        entityManager.getTransaction().begin();
        Produto produto = entityManager.find(Produto.class, 1);
        produto.setFoto(carregarFoto());
        entityManager.getTransaction().commit();

        entityManager.clear();

        Produto produtoVerificacao = entityManager.find(Produto.class, 1);
        Assertions.assertNotNull(produtoVerificacao.getFoto());
        Assertions.assertTrue(produtoVerificacao.getFoto().length > 0);
    }

    private static byte[] carregarNotaFiscal() {
        return carregarArquivo("/nota-fiscal.xml");
    }

    private static byte[] carregarFoto() {
        return carregarArquivo("/kindle.jpg");
    }

    private static byte[] carregarArquivo(String nome) {
        try {
            return Objects.requireNonNull(SalvandoArquivosTest.class.getResourceAsStream(nome)).readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}