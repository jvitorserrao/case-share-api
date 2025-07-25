package com.share.case_jr.model.dto;

import lombok.Builder;

@Builder
public record LivroFiltersDTO(
        Integer idCategoria,
        Integer ano,
        String nomeAutor
) {
}
