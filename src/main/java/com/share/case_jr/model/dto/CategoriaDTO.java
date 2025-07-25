package com.share.case_jr.model.dto;


import lombok.Builder;

@Builder
public record CategoriaDTO(
        String nome,
        String descricao
) {
}
