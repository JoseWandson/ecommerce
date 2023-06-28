package com.wandson.ecommerce.jpql;

import com.wandson.ecommerce.EntityManagerTest;
import com.wandson.ecommerce.dto.ProdutoDTO;
import com.wandson.ecommerce.model.Cliente;
import com.wandson.ecommerce.model.Pedido;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class BasicoJPQLTest extends EntityManagerTest {

    @Test
    void buscarPorIdentificador() {
        // entityManager.find(Pedido.class, 1)

        TypedQuery<Pedido> typedQuery = entityManager
                .createQuery("select p from Pedido p where p.id = 1", Pedido.class);

        Pedido pedido = typedQuery.getSingleResult();
        Assertions.assertNotNull(pedido);
    }

    @Test
    void mostrarDiferencaQueries() {
        String jpql = "select p from Pedido p where p.id = 1";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        Pedido pedido1 = typedQuery.getSingleResult();
        Assertions.assertNotNull(pedido1);

        Query query = entityManager.createQuery(jpql);
        Pedido pedido2 = (Pedido) query.getSingleResult();
        Assertions.assertNotNull(pedido2);
    }

    @Test
    void selecionarUmAtributoParaRetorno() {
        String jpql = "select p.nome from Produto p";

        TypedQuery<String> typedQuery = entityManager.createQuery(jpql, String.class);
        List<String> lista = typedQuery.getResultList();
        Assertions.assertEquals(String.class, lista.get(0).getClass());

        String jpqlCliente = "select p.cliente from Pedido p";
        TypedQuery<Cliente> typedQueryCliente = entityManager.createQuery(jpqlCliente, Cliente.class);
        List<Cliente> listaClientes = typedQueryCliente.getResultList();
        Assertions.assertEquals(Cliente.class, listaClientes.get(0).getClass());
    }

    @Test
    void projetarOResultado() {
        String jpql = "select p.id, p.nome from Produto p";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = typedQuery.getResultList();

        Assertions.assertEquals(2, lista.get(0).length);

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    void projetarNoDTO() {
        String jpql = "select new com.wandson.ecommerce.dto.ProdutoDTO(p.id, p.nome) from Produto p";

        TypedQuery<ProdutoDTO> typedQuery = entityManager.createQuery(jpql, ProdutoDTO.class);
        List<ProdutoDTO> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println(p.getId() + ", " + p.getNome()));
    }

    @Test
    void ordenarResultados() {
        String jpql = "select c from Cliente c order by c.nome asc"; // desc

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(jpql, Cliente.class);

        List<Cliente> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(c -> System.out.println(c.getId() + ", " + c.getNome()));
    }

    @Test
    void usarDistinct() {
        var jpql = """
                select distinct p
                from Pedido p
                         join p.itens i
                         join i.produto pro
                where pro.id in (1, 2, 3, 4)""";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);

        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        System.out.println(lista.size());
    }
}