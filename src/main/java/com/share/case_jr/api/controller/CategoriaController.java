package com.share.case_jr.api.controller;

import com.share.case_jr.model.dto.CategoriaDTO;
import com.share.case_jr.model.dto.LivroResumoDTO;
import com.share.case_jr.model.entity.Categoria;
import com.share.case_jr.model.entity.Livro;
import com.share.case_jr.model.service.CategoriaService;
import com.share.case_jr.model.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categorias", description = "Requisições relacionada as categorias")
@RestController
@RequiredArgsConstructor
@RequestMapping("/categoria")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final LivroService livroService;

    @Operation(summary = "Listar todas")
    @GetMapping
    public List<Categoria> listar() {
        return categoriaService.getAll();
    }

    @Operation(summary = "Criar nova categoria")
    @PostMapping
    public ResponseEntity<Categoria> criar(@RequestBody @Valid CategoriaDTO dto) {
        var cat = new Categoria();
        BeanUtils.copyProperties(dto, cat);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.save(cat));
    }

    @Operation(summary = "Livros da Categoria")
    @GetMapping("/search")
    public List<Livro> buscarPorTitulo(@RequestParam Long idCategoria) {
        return livroService.listarLivrosCategoria(idCategoria);
    }
}