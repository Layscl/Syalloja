package com.sistema.vendas.tenis.JpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;

//import com.sistema.vendas.tenis.Model.Categoria;
//import com.sistema.vendas.tenis.Model.ItemPedido;
import com.sistema.vendas.tenis.Model.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}