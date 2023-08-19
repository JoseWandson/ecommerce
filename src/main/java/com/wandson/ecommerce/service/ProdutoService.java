package com.wandson.ecommerce.service;

import com.wandson.ecommerce.model.Produto;
import com.wandson.ecommerce.repository.Produtos;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final Produtos produtos;

    @Transactional
    public Produto criar(Produto produto) {
        produto.setDataCriacao(LocalDateTime.now());

        return produtos.salvar(produto);
    }

    @Transactional
    public void atualizar(Integer id, Map<String, Object> produto) {
        Produto produtoAtual = produtos.buscar(id);

        try {
            BeanUtils.populate(produtoAtual, produto);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        produtoAtual.setDataUltimaAtualizacao(LocalDateTime.now());

        produtos.salvar(produtoAtual);
    }
}