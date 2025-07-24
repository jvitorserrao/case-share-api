package com.example.demo.model.service.impl;

import com.example.demo.api.exception.ApiErrorException;
import com.example.demo.model.entity.Autor;
import com.example.demo.model.repository.AutorRepository;
import com.example.demo.model.service.AutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AutorServiceImpl implements AutorService {

    private final AutorRepository autorRepository;

    @Override
    public Autor findById(Long id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new ApiErrorException("Autor não encontrado com ID: " + id));
    }
}