package com.example.demo.model.service;

import com.example.demo.model.entity.Livro;

import java.util.List;
import java.util.Optional;

public interface LivroService {

    Livro save(Livro livro);
    Optional<Livro> findByIsbn(String isbn);
    List<Livro> getAll();
}
