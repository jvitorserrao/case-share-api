package com.example.demo.model.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record AutorDTO(
        Long id,
        String nome,
        String email,
        LocalDate dataNascimento
) {}
