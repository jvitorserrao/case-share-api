package com.example.demo.model.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record LivroResumoDTO(
        Long id,
        String titulo,
        String isbn,
        Integer anoPublicacao,
        BigDecimal preco
) {}

