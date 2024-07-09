package com.sistema.vendas.tenis.Controller;

import com.sistema.vendas.tenis.JpaRepository.ClienteRepository;
import com.sistema.vendas.tenis.JpaRepository.PedidoRepository;
import com.sistema.vendas.tenis.Model.Cliente;
import com.sistema.vendas.tenis.Model.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin("*")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping
    public ResponseEntity<?> finalizarPedido(@RequestBody Pedido pedido) {
        try {
            // Define a data atual como data do pedido
            pedido.setData(new Date());
            // Define o status inicial do pedido
            pedido.setStatus("Em processamento");

            // Obtém o cliente do pedido recebido
            Cliente cliente = pedido.getCliente();
            if (cliente == null) {
                return ResponseEntity.badRequest().body("Cliente não especificado no pedido.");
            }

            // Verifica se o cliente existe no banco de dados
            Long clienteId = cliente.getId();
            Optional<Cliente> clienteOptional = clienteRepository.findById(clienteId);
            if (!clienteOptional.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            // Associa o cliente encontrado ao pedido
            pedido.setCliente(clienteOptional.get());

            // Salva o pedido no banco de dados
            Pedido pedidoFinalizado = pedidoRepository.save(pedido);

            return ResponseEntity.ok(pedidoFinalizado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao processar pedido: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarPedidos() {
        try {
            List<Pedido> pedidos = pedidoRepository.findAll();
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao listar pedidos: " + e.getMessage());
        }
    }
}
