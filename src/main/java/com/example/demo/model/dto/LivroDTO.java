package com.example.demo.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record LivroDTO (
        @NotBlank String url,
        @NotNull Long autorId,
        @NotNull Long categoriaId
){
}
