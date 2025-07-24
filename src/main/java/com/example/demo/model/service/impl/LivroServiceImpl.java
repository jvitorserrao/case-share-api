package com.example.demo.model.service.impl;

import com.example.demo.api.exception.ApiErrorException;
import com.example.demo.model.entity.Livro;
import com.example.demo.model.repository.LivroRepository;
import com.example.demo.model.service.LivroService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LivroServiceImpl implements LivroService {

    private final LivroRepository livroRepository;

    @Override
    public Livro save(Livro livro) {
        try {
            return livroRepository.save(livro);
        } catch (Exception e) {
            log.error("Falha ao salvar livro: " + livro.getTitulo(), e);
            throw new ApiErrorException("Não foi possível salvar o livro!");
        }
    }

    @Override
    public Optional<Livro> findByIsbn(String isbn) {
        return livroRepository.findByIsbn(isbn);
    }


}