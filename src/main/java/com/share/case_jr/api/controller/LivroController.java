package com.share.case_jr.api.controller;


import com.share.case_jr.model.dto.LivroFiltersDTO;
import com.share.case_jr.model.dto.LivroResumoDTO;
import com.share.case_jr.model.dto.RequestDTO;
import com.share.case_jr.model.entity.Autor;
import com.share.case_jr.model.entity.Categoria;
import com.share.case_jr.model.entity.Livro;
import com.share.case_jr.model.service.AutorService;
import com.share.case_jr.model.service.CategoriaService;
import com.share.case_jr.model.service.LivroService;
import com.share.case_jr.model.service.ScrapingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Livros", description = "Requisições relacionadas aos livros")
@RestController
@RequiredArgsConstructor
@RequestMapping("/livro")
public class LivroController {

    private final ScrapingService scrapingService;
    private final LivroService livroService;
    private final AutorService autorService;
    private final CategoriaService categoriaService;

    @Operation(summary = "Realizar importação de um livro da livraria Amazon")
    @PostMapping("/importar")
    public ResponseEntity<String> importar(@Valid @RequestBody RequestDTO dto){
        scrapingService.importarLivro(dto);
        return ResponseEntity.status(HttpStatus.OK).body("Livro importado com sucesso!");
    }

    @Operation(summary = "Listar todos com filtros: categoria, ano, autor")
    @GetMapping
    public List<Livro> listarComFiltros(
            @RequestParam(required = false) Integer idCategoria,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) String nomeAutor
    ) {
        LivroFiltersDTO filters = LivroFiltersDTO.builder()
                .idCategoria(idCategoria)
                .ano(ano)
                .nomeAutor(nomeAutor)
                .build();

        return livroService.getAll(filters);
    }

    @Operation(summary = "Buscar por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarPorId(@PathVariable Long id) {
        Livro livro = livroService.findById(id);
        return ResponseEntity.ok(livro);

    }

    @PostMapping
    @Operation(summary = "Criar novo livro")
    public ResponseEntity<Livro> criar(@RequestBody @Valid LivroResumoDTO dto) {
        var livro = new Livro();
        BeanUtils.copyProperties(dto, livro);

        Autor autor = autorService.findById(dto.idAutor());
        Categoria categoria = categoriaService.findById(dto.idCategoria());
        livro.setAutor(autor);
        livro.setCategoria(categoria);

        return ResponseEntity.status(HttpStatus.CREATED).body(livroService.save(livro));
    }


    @Operation(summary = "Atualizar livro")
    @PutMapping("/{id}")
    public Livro atualizar(@PathVariable Long id, @RequestBody LivroResumoDTO dto) {
        return livroService.atualizar(id, dto);
    }

    @Operation(summary = "Deletar livro")
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        livroService.excluir(id);
    }

    @Operation(summary = "Buscar por título")
    @GetMapping("/search")
    public Livro buscarPorTitulo(@RequestParam String titulo) {
        return livroService.buscarPorTitulo(titulo);
    }
}