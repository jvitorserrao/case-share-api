package com.share.case_jr.model.service;

import com.share.case_jr.model.dto.LivroFiltersDTO;
import com.share.case_jr.model.dto.LivroResumoDTO;
import com.share.case_jr.model.entity.Livro;

import java.util.List;
import java.util.Optional;

public interface LivroService {

    Livro save(Livro livro);
    Optional<Livro> findByIsbn(String isbn);
    List<Livro> getAll(LivroFiltersDTO filters);

    Livro findById(Long id);
    List<Livro> listarLivrosCategoria(Long idCategoria);
    Livro atualizar(Long id, LivroResumoDTO autor);

    void excluir(Long id);

    Livro buscarPorTitulo(String titulo);
}
