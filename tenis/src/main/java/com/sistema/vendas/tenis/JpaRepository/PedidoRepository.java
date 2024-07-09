package com.sistema.vendas.tenis.JpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;

//import com.sistema.vendas.tenis.Model.Cliente;
//import com.sistema.vendas.tenis.Model.Categoria;
//import com.sistema.vendas.tenis.Model.Produto;
import com.sistema.vendas.tenis.Model.Pedido;


public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}