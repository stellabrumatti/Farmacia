package com.generation.farmacia.controller;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.generation.farmacia.model.Categoria;
import com.generation.farmacia.model.Produto;
import com.generation.farmacia.repository.CategoriaRepository;
import com.generation.farmacia.repository.ProdutoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // GET /produtos
    @GetMapping
    public ResponseEntity<List<Produto>> getAll() {
        return ResponseEntity.ok(produtoRepository.findAll());
    }

    // GET /produtos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Produto> getById(@PathVariable Long id) {
        return produtoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // GET /produtos/nome/{nome}  (busca parcial, case-insensitive)
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Produto>> getAllByNome(@PathVariable String nome) {
        List<Produto> produtos = produtoRepository.findAllByNomeContainingIgnoreCase(nome);
        if (produtos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(produtos);
    }

    // GET /produtos/categoria/{nomeCategoria} (opcional — busca por nome da categoria)
    @GetMapping("/categoria/{nomeCategoria}")
    public ResponseEntity<List<Produto>> getAllByCategoria(@PathVariable String nomeCategoria) {
        List<Produto> produtos = produtoRepository.findAllByCategoriaNomeContainingIgnoreCase(nomeCategoria);
        if (produtos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(produtos);
    }

    // POST /produtos
    @PostMapping
    public ResponseEntity<?> postProduto(@Valid @RequestBody Produto produto) {
        // se veio categoria preenchida, validar existência
        if (produto.getCategoria() != null && produto.getCategoria().getId() != null) {
            Optional<Categoria> optCat = categoriaRepository.findById(produto.getCategoria().getId());
            if (optCat.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Categoria com id " + produto.getCategoria().getId() + " não encontrada.");
            }
            produto.setCategoria(optCat.get());
        } else {
            // opcional: exigir categoria para produto — ajuste conforme regra do seu domínio
            produto.setCategoria(null);
        }

        Produto saved = produtoRepository.save(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT /produtos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> putProduto(@PathVariable Long id, @Valid @RequestBody Produto produto) {
        return produtoRepository.findById(id).map(existing -> {
            existing.setNome(produto.getNome());
            existing.setDescricao(produto.getDescricao());
            existing.setPreco(produto.getPreco());
           
            // categoria: validar se informado
            if (produto.getCategoria() != null && produto.getCategoria().getId() != null) {
                Optional<Categoria> optCat = categoriaRepository.findById(produto.getCategoria().getId());
                if (optCat.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Categoria com id " + produto.getCategoria().getId() + " não encontrada.");
                }
                existing.setCategoria(optCat.get());
            } else {
                existing.setCategoria(null);
            }
            Produto updated = produtoRepository.save(existing);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // DELETE /produtos/{id}
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Optional<Produto> tema = produtoRepository.findById(id);
        
        if(tema.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        
        produtoRepository.deleteById(id);  
    }
}

