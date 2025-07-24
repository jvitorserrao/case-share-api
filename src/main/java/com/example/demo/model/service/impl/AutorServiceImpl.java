package com.example.demo.model.service.impl;

import com.example.demo.api.exception.ApiErrorException;
import com.example.demo.model.dto.AutorDTO;
import com.example.demo.model.entity.Autor;
import com.example.demo.model.entity.Livro;
import com.example.demo.model.repository.AutorRepository;
import com.example.demo.model.repository.LivroRepository;
import com.example.demo.model.service.AutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AutorServiceImpl implements AutorService {

    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;

    @Override
    public Autor save(Autor autor) {
        return autorRepository.save(autor);
    }

    @Override
    public Autor findById(Long id) {
        return autorRepository.findById(id).orElseThrow(() -> new ApiErrorException("Autor não encontrado"));
    }

    @Override
    public Page<Autor> findAll(Pageable pageable) {
        return autorRepository.findAll(pageable);
    }


    @Override
    public Autor atualizar(Long id, AutorDTO dto) {
        Autor existente = findById(id);

        existente.setNome(dto.nome());
        existente.setEmail(dto.email());
        existente.setDataNascimento(dto.dataNascimento());

        return autorRepository.save(existente);

    }

    public void excluir(Long id) {
        List<Livro> livros = livroRepository.findByAutorId(id);
        livroRepository.deleteAll(livros);
        autorRepository.deleteById(id);
    }


    @Override
    public List<Livro> listarLivrosDoAutor(Long idAutor) {
        return livroRepository.findByAutorId(idAutor);
    }

}