package com.example.demo.api.controller;

import com.example.demo.model.dto.RequestDTO;
import com.example.demo.model.entity.Autor;
import com.example.demo.model.entity.Livro;
import com.example.demo.model.service.LivroService;
import com.example.demo.model.service.ScrapingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Api dados Livros")
@RestController
@RequiredArgsConstructor
@RequestMapping("/livro")
public class LivroController {

    private final ScrapingService scrapingService;
    private final LivroService livroService;

    @PostMapping("/importar")
    public ResponseEntity<String> importar(@Valid @RequestBody RequestDTO dto){
        scrapingService.importarLivro(dto);
        return ResponseEntity.status(HttpStatus.OK).body("Livro importado com sucesso!");
    }

    @GetMapping
    public List<Livro> listar() {
        return livroService.getAll();
    }
}
