package com.sistema.vendas.tenis.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ItemPedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private BigDecimal preco;

    // Removendo as anotações ManyToOne e JoinColumn por enquanto
    // @ManyToOne
    // @JoinColumn(name = "id_pedido", nullable = false)
    // private Pedido pedido;

    // @ManyToOne
    // @JoinColumn(name = "id_produto", nullable = false)
    // private Produto produto;

    // Getters e setters omitidos para brevidade

    // public Pedido getPedido() {
    //     return pedido;
    // }

    // public void setPedido(Pedido pedido) {
    //     this.pedido = pedido;
    // }

    // public Produto getProduto() {
    //     return produto;
    // }

    // public void setProduto(Produto produto) {
    //     this.produto = produto;
    // }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
}
