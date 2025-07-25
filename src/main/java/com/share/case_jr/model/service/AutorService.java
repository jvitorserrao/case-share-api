package com.share.case_jr.model.service;

import com.share.case_jr.model.dto.AutorDTO;
import com.share.case_jr.model.entity.Autor;
import com.share.case_jr.model.entity.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface AutorService {

    Autor save(Autor autor);

    Page<Autor> findAll(Pageable pageable);

    Autor findById(Long id);

    Autor atualizar(Long id, AutorDTO autor);

    void excluir(Long id);

    List<Livro> listarLivrosDoAutor(Long idAutor);

}