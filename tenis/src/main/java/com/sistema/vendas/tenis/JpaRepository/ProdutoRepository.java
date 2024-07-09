package com.sistema.vendas.tenis.JpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;

//import com.sistema.vendas.tenis.Model.Categoria;
import com.sistema.vendas.tenis.Model.Produto;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByCategoriaNomeIgnoreCase(String nomeCategoria);
}
