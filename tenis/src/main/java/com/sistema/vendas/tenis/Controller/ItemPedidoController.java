package com.sistema.vendas.tenis.Controller;

import com.sistema.vendas.tenis.JpaRepository.ItemPedidoRepository;
import com.sistema.vendas.tenis.Model.ItemPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/itempedidos")
public class ItemPedidoController {

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @GetMapping
    public ResponseEntity<List<ItemPedido>> listarItemPedidos() {
        List<ItemPedido> itemPedidos = itemPedidoRepository.findAll();
        return ResponseEntity.ok(itemPedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemPedido> buscarItemPedidoPorId(@PathVariable Long id) {
        Optional<ItemPedido> itemPedidoOptional = itemPedidoRepository.findById(id);
        return itemPedidoOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ItemPedido> adicionarItemPedido(@RequestBody ItemPedido itemPedido) {
        try {
            ItemPedido novoItemPedido = itemPedidoRepository.save(itemPedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoItemPedido);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarItemPedido(@PathVariable Long id, @RequestBody ItemPedido itemPedidoAtualizado) {
        Optional<ItemPedido> itemPedidoOptional = itemPedidoRepository.findById(id);
        if (itemPedidoOptional.isPresent()) {
            try {
                ItemPedido itemPedido = itemPedidoOptional.get();
                itemPedido.setQuantidade(itemPedidoAtualizado.getQuantidade());
                itemPedido.setPreco(itemPedidoAtualizado.getPreco());

                itemPedidoRepository.save(itemPedido);
                return ResponseEntity.ok("Item do pedido atualizado com sucesso.");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item do pedido não encontrado.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarItemPedido(@PathVariable Long id) {
        Optional<ItemPedido> itemPedidoOptional = itemPedidoRepository.findById(id);
        if (itemPedidoOptional.isPresent()) {
            itemPedidoRepository.deleteById(id);
            return ResponseEntity.ok("Item do pedido deletado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item do pedido não encontrado.");
        }
    }

}

