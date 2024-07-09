package com.sistema.vendas.tenis.JpaRepository;

import com.sistema.vendas.tenis.Model.ItemPedido;
import com.sistema.vendas.tenis.Model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    static List<ItemPedido> findByProduto(Produto produto) {
        return null;
    }

}
