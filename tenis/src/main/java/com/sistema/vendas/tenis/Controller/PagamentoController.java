package com.sistema.vendas.tenis.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sistema.vendas.tenis.JpaRepository.PagamentoRepository;
import com.sistema.vendas.tenis.Model.Pagamento;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @GetMapping
    public ResponseEntity<List<Pagamento>> listarPagamentos() {
        List<Pagamento> pagamentos = pagamentoRepository.findAll();
        return ResponseEntity.ok(pagamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> buscarPagamentoPorId(@PathVariable Long id) {
        Optional<Pagamento> pagamentoOptional = pagamentoRepository.findById(id);
        return pagamentoOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pagamento adicionarPagamento(@RequestBody Pagamento pagamento) {
        return pagamentoRepository.save(pagamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarPagamento(@PathVariable Long id, @RequestBody Pagamento pagamentoAtualizado) {
        Optional<Pagamento> pagamentoOptional = pagamentoRepository.findById(id);
        if (pagamentoOptional.isPresent()) {
            Pagamento pagamento = pagamentoOptional.get();
            pagamento.setMetodo(pagamentoAtualizado.getMetodo());
            pagamento.setStatus(pagamentoAtualizado.getStatus());
            pagamento.setValor(pagamentoAtualizado.getValor());
            pagamento.setData(pagamentoAtualizado.getData());
            pagamento.setPedido(pagamentoAtualizado.getPedido());
            pagamentoRepository.save(pagamento);
            return ResponseEntity.ok("Pagamento atualizado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pagamento não encontrado.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarPagamento(@PathVariable Long id) {
        Optional<Pagamento> pagamentoOptional = pagamentoRepository.findById(id);
        if (pagamentoOptional.isPresent()) {
            pagamentoRepository.deleteById(id);
            return ResponseEntity.ok("Pagamento deletado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pagamento não encontrado.");
        }
    }
}
