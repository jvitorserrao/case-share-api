package com.share.case_jr.api.controller;

import com.share.case_jr.model.dto.AutorDTO;
import com.share.case_jr.model.entity.Autor;
import com.share.case_jr.model.entity.Livro;
import com.share.case_jr.model.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "Autores", description = "Requisições relacionadas aos autores")
@RestController
@RequiredArgsConstructor
@RequestMapping("/autor")
public class AutorController {

    private final AutorService autorService;

    @Operation(summary = "Listar todos (com paginação)")
    @GetMapping
    public Page<Autor> listar(Pageable pageable) {
        return autorService.findAll(pageable);
    }

    @Operation(summary = "Buscar por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Autor> buscarPorId(@PathVariable Long id) {
        Autor autor = autorService.findById(id);
        return ResponseEntity.ok(autor);

    }

    @Operation(summary = "Criar novo autor")
    @PostMapping
    public ResponseEntity<Autor> criar(@RequestBody @Valid AutorDTO autorDTO) {
        var autor = new Autor();
        BeanUtils.copyProperties(autorDTO, autor);
        return ResponseEntity.status(HttpStatus.CREATED).body(autorService.save(autor));

    }

    @Operation(summary = "Atualizar autor")
    @PutMapping("/{id}")
    public Autor atualizar(@PathVariable Long id, @RequestBody AutorDTO autor) {
        return autorService.atualizar(id, autor);
    }

    @Operation(summary = "Deletar autor")
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        autorService.excluir(id);
    }

    @Operation(summary = " Listar livros do autor")
    @GetMapping("/{idAutor}/livros")
    public List<Livro> listarLivrosDoAutor(@PathVariable Long idAutor) {
        return autorService.listarLivrosDoAutor(idAutor);
    }
}