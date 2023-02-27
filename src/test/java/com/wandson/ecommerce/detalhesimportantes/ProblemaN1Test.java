package com.wandson.ecommerce.detalhesimportantes;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Pedido;
import com.wandson.ecommerce.model.Pedido_;
import jakarta.persistence.EntityGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ProblemaN1Test extends EntityManagerTest {

    @Test
    @SuppressWarnings("unchecked")
    void resolverComEntityGraph() {
        EntityGraph<Pedido> entityGraph = entityManager.createEntityGraph(Pedido.class);
        entityGraph.addAttributeNodes(Pedido_.cliente, Pedido_.notaFiscal, Pedido_.pagamento);

        List<Pedido> lista = entityManager
                .createQuery("select p from Pedido p ", Pedido.class)
                .setHint("javax.persistence.loadgraph", entityGraph)
                .getResultList();

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void resolverComFetch() {
        var query = """
                select p from Pedido p
                    join fetch p.cliente c
                    join fetch p.pagamento pag
                    join fetch p.notaFiscal nf""";

        List<Pedido> lista = entityManager.createQuery(query, Pedido.class).getResultList();

        Assertions.assertFalse(lista.isEmpty());
    }
}