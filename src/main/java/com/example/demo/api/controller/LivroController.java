package com.example.demo.api.controller;

import com.example.demo.model.dto.LivroDTO;
import com.example.demo.model.service.ScrapingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Api dados Livros")
@RestController
@RequiredArgsConstructor
@RequestMapping("/livro")
public class LivroController {

    private final ScrapingService scrapingService;

    @PostMapping("/importar")
    public ResponseEntity<String> importar(@Valid @RequestBody LivroDTO dto){
        scrapingService.importarLivro(dto);
        return ResponseEntity.status(HttpStatus.OK).body("Livro importado com sucesso!");
    }
}
