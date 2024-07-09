package com.sistema.vendas.tenis.Controller;

import com.sistema.vendas.tenis.JpaRepository.ClienteRepository;
import com.sistema.vendas.tenis.Model.Cliente;
import com.sistema.vendas.tenis.Model.Nivel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
@CrossOrigin("*") // Ajuste o domínio conforme necessário
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    // Endpoint para listar todos os clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return ResponseEntity.ok(clientes);
    }

    // Endpoint para buscar um cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Long id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        return clienteOptional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para adicionar um novo cliente
    @PostMapping
    public ResponseEntity<?> adicionarCliente(@RequestBody Cliente cliente) {
        try {
            Cliente novoCliente = clienteRepository.save(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Email já cadastrado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar requisição: " + e.getMessage());
        }
    }

    // Endpoint para atualizar um cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteAtualizado) {
        try {
            Optional<Cliente> clienteOptional = clienteRepository.findById(id);
            if (clienteOptional.isPresent()) {
                Cliente cliente = clienteOptional.get();
                cliente.setNome(clienteAtualizado.getNome());
                cliente.setEmail(clienteAtualizado.getEmail());
                cliente.setSenha(clienteAtualizado.getSenha());
                //cliente.setUsuario(clienteAtualizado.getUsuario()); // Adicionado para atualização de usuário
                cliente.setNivel(clienteAtualizado.getNivel()); // Adicionado para atualização de nível
                Cliente clienteSalvo = clienteRepository.save(cliente);
                return ResponseEntity.ok(clienteSalvo);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar requisição: " + e.getMessage());
        }
    }

    // Endpoint para deletar um cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarCliente(@PathVariable Long id) {
        try {
            Optional<Cliente> clienteOptional = clienteRepository.findById(id);
            if (clienteOptional.isPresent()) {
                clienteRepository.deleteById(id);
                return ResponseEntity.ok("Cliente deletado com sucesso.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar requisição: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginCliente(@RequestBody Cliente clienteLogin) {
        try {
            Optional<Cliente> clienteOptional = clienteRepository.findByEmailAndSenha(clienteLogin.getEmail(), clienteLogin.getSenha());
            if (clienteOptional.isPresent()) {
                Cliente cliente = clienteOptional.get();
                // Verifica se o cliente é administrador pelo ID
                if (cliente.getNivel().getId() == 1) { // Supondo que ID 1 seja o administrador
                    return ResponseEntity.ok("Admin");
                } else {
                    return ResponseEntity.ok("Cliente");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas. Verifique seu email e senha.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar requisição: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutCliente(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Obtém a sessão sem criar uma nova, se não existir
        if (session != null) {
            session.invalidate(); // Invalida a sessão existente
        }
        return ResponseEntity.ok("Cliente desconectado com sucesso.");
    }
}
