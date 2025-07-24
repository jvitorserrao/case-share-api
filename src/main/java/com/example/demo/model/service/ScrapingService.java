package com.example.demo.model.service;

import com.example.demo.model.dto.RequestDTO;
import com.example.demo.model.entity.Livro;

public interface ScrapingService {
    Livro importarLivro(RequestDTO dto);
}
