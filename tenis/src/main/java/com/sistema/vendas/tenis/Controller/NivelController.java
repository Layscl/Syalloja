package com.sistema.vendas.tenis.Controller;

import com.sistema.vendas.tenis.Model.Nivel;
import com.sistema.vendas.tenis.JpaRepository.NivelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/niveis")
@CrossOrigin("*") // Ajuste o domínio conforme necessário
public class NivelController {

    private final NivelRepository nivelRepository;

    @Autowired
    public NivelController(NivelRepository nivelRepository) {
        this.nivelRepository = nivelRepository;
    }

    // Endpoint para buscar todos os níveis de cliente
    @GetMapping
    public ResponseEntity<List<Nivel>> getAllNiveis() {
        List<Nivel> niveis = nivelRepository.findAll();
        return new ResponseEntity<>(niveis, HttpStatus.OK);
    }

    // Endpoint para buscar um nível específico por ID
    @GetMapping("/{id}")
    public ResponseEntity<Nivel> getNivelById(@PathVariable Long id) {
        Nivel nivel = nivelRepository.findById(id)
                .orElse(null); // Caso não encontre, retorna null ou pode ser ajustado para retornar HttpStatus.NOT_FOUND
        if (nivel != null) {
            return new ResponseEntity<>(nivel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para criar um novo nível de cliente
    @PostMapping
    public ResponseEntity<Nivel> createNivel(@RequestBody Nivel nivel) {
        try {
            Nivel novoNivel = nivelRepository.save(nivel);
            return new ResponseEntity<>(novoNivel, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // Endpoint para deletar um nível existente
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteNivel(@PathVariable Long id) {
        try {
            nivelRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
