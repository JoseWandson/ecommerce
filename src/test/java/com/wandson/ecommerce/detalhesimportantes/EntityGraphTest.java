package com.wandson.ecommerce.detalhesimportantes;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.model.Cliente;
import com.wandson.ecommerce.model.Cliente_;
import com.wandson.ecommerce.model.Pedido;
import com.wandson.ecommerce.model.Pedido_;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.Subgraph;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
class EntityGraphTest extends EntityManagerTest {

    @Test
    void buscarAtributosEssenciaisDePedidoComFind() {
        EntityGraph<Pedido> entityGraph = entityManager.createEntityGraph(Pedido.class);
        entityGraph.addAttributeNodes(Pedido_.notaFiscal, Pedido_.cliente);

        Map<String, Object> properties = new HashMap<>();
//        properties.put("jakarta.persistence.fetchgraph", entityGraph);
        properties.put("jakarta.persistence.loadgraph", entityGraph);
        Pedido pedido = entityManager.find(Pedido.class, 1, properties);
        Assertions.assertNotNull(pedido);
    }

    @Test
    void buscarAtributosEssenciaisDePedidoComJPQL() {
        EntityGraph<Pedido> entityGraph = entityManager.createEntityGraph(Pedido.class);
        entityGraph.addAttributeNodes(Pedido_.notaFiscal, Pedido_.cliente);

        TypedQuery<Pedido> typedQuery = entityManager.createQuery("select p from Pedido p", Pedido.class);
        typedQuery.setHint("jakarta.persistence.fetchgraph", entityGraph);
        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void buscarAtributosEssenciaisDePedidoComJPQL02() {
        EntityGraph<Pedido> entityGraph = entityManager.createEntityGraph(Pedido.class);
        entityGraph.addAttributeNodes(Pedido_.dataCriacao, Pedido_.status, Pedido_.total);

        Subgraph<Cliente> subgraphCliente = entityGraph.addSubgraph(Pedido_.cliente);
        subgraphCliente.addAttributeNodes(Cliente_.nome, Cliente_.cpf);

        TypedQuery<Pedido> typedQuery = entityManager.createQuery("select p from Pedido p", Pedido.class);
        typedQuery.setHint("jakarta.persistence.fetchgraph", entityGraph);
        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }
}