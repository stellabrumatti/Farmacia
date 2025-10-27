package com.generation.farmacia.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import com.generation.farmacia.model.Categoria;
import com.generation.farmacia.repository.CategoriaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // GET all
    @GetMapping
    public ResponseEntity<List<Categoria>> getAll() {
        return ResponseEntity.ok(categoriaRepository.findAll());
    }

    // GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getById(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        return categoria.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    // GET by Nome
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Categoria>> getByNome(@PathVariable String nome) {
        List<Categoria> categorias = categoriaRepository.findAllByNomeContainingIgnoreCase(nome);
        if (categorias.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categorias);
    }

    // POST
    @PostMapping
    public ResponseEntity<Categoria> postCategoria(@Valid @RequestBody Categoria categoria) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(categoriaRepository.save(categoria));
    }
    
    @PutMapping
	public ResponseEntity<Categoria> put(@Valid @RequestBody Categoria categoria){
    return categoriaRepository.findById(categoria.getId())
        .map(resposta -> ResponseEntity.status(HttpStatus.CREATED)
        .body(categoriaRepository.save(categoria)))
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
    Optional<Categoria> categoria = categoriaRepository.findById(id);
    
    if(categoria.isEmpty())
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    
    categoriaRepository.deleteById(id);      
	}
}
