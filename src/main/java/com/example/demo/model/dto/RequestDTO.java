package com.example.demo.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RequestDTO(
        @NotBlank String url
){
}
