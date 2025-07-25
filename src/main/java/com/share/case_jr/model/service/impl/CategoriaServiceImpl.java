package com.share.case_jr.model.service.impl;

import com.share.case_jr.api.exception.ApiErrorException;
import com.share.case_jr.model.entity.Categoria;
import com.share.case_jr.model.repository.CategoriaRepository;
import com.share.case_jr.model.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id).orElseThrow(() -> new ApiErrorException("Categoria não encontrado"));
    }

    @Override
    public List<Categoria> getAll() {
        return categoriaRepository.findAll();
    }

}