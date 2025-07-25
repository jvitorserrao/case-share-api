package com.share.case_jr.model.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record AutorDTO(
        Long id,
        String nome,
        String email,
        LocalDate dataNascimento
) {}
