package com.example.demo.api.controller;

import com.example.demo.model.dto.AutorDTO;
import com.example.demo.model.entity.Autor;
import com.example.demo.model.entity.Livro;
import com.example.demo.model.service.AutorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Api dados Autor")
@RestController
@RequiredArgsConstructor
@RequestMapping("/autor")
public class AutorController {

    private final AutorService autorService;


    // OK
    @GetMapping
    public Page<Autor> listar(Pageable pageable) {
        return autorService.findAll(pageable);
    }

    // OK
    @GetMapping("/{id}")
    public ResponseEntity<Autor> buscarPorId(@PathVariable Long id) {
        Autor autor = autorService.findById(id);
        return ResponseEntity.ok(autor);

    }

    // OK
    @PostMapping
    public ResponseEntity<Autor> criar(@RequestBody @Valid AutorDTO autorDTO) {
        var autor = new Autor();
        BeanUtils.copyProperties(autorDTO, autor);
        return ResponseEntity.status(HttpStatus.CREATED).body(autorService.save(autor));

    }

    // OK
    @PutMapping("/{id}")
    public Autor atualizar(@PathVariable Long id, @RequestBody AutorDTO autor) {
        return autorService.atualizar(id, autor);
    }

    // OK
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        autorService.excluir(id);
    }

    // OK
    @GetMapping("/{idAutor}/livros")
    public List<Livro> listarLivrosDoAutor(@PathVariable Long idAutor) {
        return autorService.listarLivrosDoAutor(idAutor);
    }
}