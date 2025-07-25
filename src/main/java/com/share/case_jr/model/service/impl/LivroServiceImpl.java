package com.share.case_jr.model.service.impl;

import com.share.case_jr.api.exception.ApiErrorException;
import com.share.case_jr.model.dto.AutorDTO;
import com.share.case_jr.model.dto.LivroFiltersDTO;
import com.share.case_jr.model.dto.LivroResumoDTO;
import com.share.case_jr.model.entity.Autor;
import com.share.case_jr.model.entity.Categoria;
import com.share.case_jr.model.entity.Livro;
import com.share.case_jr.model.repository.AutorRepository;
import com.share.case_jr.model.repository.CategoriaRepository;
import com.share.case_jr.model.repository.LivroRepository;
import com.share.case_jr.model.service.AutorService;
import com.share.case_jr.model.service.CategoriaService;
import com.share.case_jr.model.service.LivroService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LivroServiceImpl implements LivroService {

    private final LivroRepository livroRepository;
    private final CategoriaService categoriaService;
    private final AutorService autorService;

    @Override
    public Livro save(Livro livro) {
        try {
            return livroRepository.save(livro);
        } catch (Exception e) {
            log.error("Erro ao salvar livro: " + livro.getTitulo(), e);
            throw new ApiErrorException("Não foi possível salvar o livro!");
        }
    }

    @Override
    public Optional<Livro> findByIsbn(String isbn) {
        return livroRepository.findByIsbn(isbn);
    }

    public List<Livro> getAll(LivroFiltersDTO filter) {
        return livroRepository.findAllByQuery(
                filter.idCategoria(),
                filter.ano(),
                filter.nomeAutor()
        );
    }

    @Override
    public Livro findById(Long id) {
        return livroRepository.findById(id).orElseThrow(() -> new ApiErrorException("Livro não encontrado"));
    }

    @Override
    public List<Livro> listarLivrosCategoria(Long idCategoria) {
        return livroRepository.findByCategoriaId(idCategoria);
    }

    @Override
    public Livro atualizar(Long id, LivroResumoDTO dto) {
        Livro atual = findById(id);
        Autor autor = autorService.findById(dto.idAutor());
        Categoria categoria = categoriaService.findById(dto.idCategoria());

        atual.setTitulo(dto.titulo());
        atual.setAutor(autor);
        atual.setIsbn(dto.isbn());
        atual.setCategoria(categoria);
        atual.setPreco(dto.preco());
        atual.setAnoPublicacao(dto.anoPublicacao());
        return livroRepository.save(atual);
    }

    public void excluir(Long id) {
        livroRepository.deleteById(id);
    }

    @Override
    public Livro buscarPorTitulo(String titulo) {
        return livroRepository.findByTitulo(titulo);
    }
}