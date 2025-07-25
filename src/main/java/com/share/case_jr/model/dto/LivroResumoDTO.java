package com.share.case_jr.model.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record LivroResumoDTO(
        Long id,
        String titulo,
        String isbn,
        Integer anoPublicacao,
        BigDecimal preco,
        Long idCategoria,
        Long idAutor
) {}

