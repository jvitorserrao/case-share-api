package com.example.demo.model.service.impl;

import com.example.demo.model.entity.Categoria;
import com.example.demo.model.repository.CategoriaRepository;
import com.example.demo.model.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

}