package com.sistema.vendas.tenis.Controller;

import com.sistema.vendas.tenis.JpaRepository.ProdutoRepository;
import com.sistema.vendas.tenis.JpaRepository.CategoriaRepository;
import com.sistema.vendas.tenis.Model.Produto;
import com.sistema.vendas.tenis.Model.Categoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
@CrossOrigin("*")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos() {
        List<Produto> produtos = produtoRepository.findAll();
        if (produtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable Long id) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        return produtoOptional.map(ResponseEntity::ok)
                              .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> adicionarProduto(
            @RequestParam("nomeProduto") String nome,
            @RequestParam("descricaoProduto") String descricao,
            @RequestParam("precoProduto") BigDecimal preco,
            @RequestParam("quantidadeProduto") Integer quantidade,
            @RequestParam("categoriaProduto") Long categoriaId,
            @RequestParam("imagemProduto") MultipartFile imagem
    ) {
        try {
            Produto produto = new Produto();
            produto.setNome(nome);
            produto.setDescricao(descricao);
            produto.setPreco(preco);
            produto.setQuantidade(quantidade);

            // Busca a categoria pelo ID e a associa ao produto
            Categoria categoria = categoriaRepository.findById(categoriaId)
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
            produto.setCategoria(categoria);

            // Salva a imagem como Blob no banco de dados
            if (!imagem.isEmpty()) {
                produto.setImagemBlob(imagem.getBytes());
            }

            Produto produtoSalvo = produtoRepository.save(produto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Produto adicionado com sucesso. ID: " + produtoSalvo.getId());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar a imagem.");
        }
    }

    @PutMapping("/{id}")
public ResponseEntity<String> atualizarProduto(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
    Optional<Produto> produtoOptional = produtoRepository.findById(id);
    if (produtoOptional.isPresent()) {
        Produto produto = produtoOptional.get();
        produto.setNome(produtoAtualizado.getNome());
        produto.setDescricao(produtoAtualizado.getDescricao());
        produto.setPreco(produtoAtualizado.getPreco());
        produto.setQuantidade(produtoAtualizado.getQuantidade());

        // Verifica se a categoria foi alterada
        if (!produto.getCategoria().getId().equals(produtoAtualizado.getCategoria().getId())) {
            Categoria categoria = categoriaRepository.findById(produtoAtualizado.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
            produto.setCategoria(categoria);
        }

        produtoRepository.save(produto);
        return ResponseEntity.ok().body("Produto atualizado com sucesso."); // Retorna um JSON com a mensagem de sucesso
    } else {
        return ResponseEntity.notFound().build(); // Retorna um status 404 caso o produto não seja encontrado
    }
}


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarProduto(@PathVariable Long id) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        if (produtoOptional.isPresent()) {
            produtoRepository.deleteById(id);
            return ResponseEntity.ok("Produto deletado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Produto>> listarProdutosPorCategoria(@PathVariable String categoria) {
        List<Produto> produtos = produtoRepository.findByCategoriaNomeIgnoreCase(categoria);
        if (produtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/imagem/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        if (produtoOptional.isPresent()) {
            Produto produto = produtoOptional.get();
            byte[] imagemBlob = produto.getImagemBlob();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // ou MediaType.IMAGE_PNG, conforme o tipo de imagem
                    .body(imagemBlob);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}