package com.share.case_jr.model.service;

import com.share.case_jr.model.entity.Categoria;

import java.util.List;


public interface CategoriaService {

    Categoria save(Categoria categoria);
    Categoria findById(Long id);
    List<Categoria> getAll();
}