package com.example.demo.model.service;

import com.example.demo.model.dto.LivroDTO;
import com.example.demo.model.entity.Livro;

public interface ScrapingService {
    Livro importarLivro(LivroDTO dto);
}
