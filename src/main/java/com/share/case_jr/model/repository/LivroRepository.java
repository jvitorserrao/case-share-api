package com.share.case_jr.model.repository;

import com.share.case_jr.model.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    Optional<Livro> findByIsbn(String isbn);
    List<Livro> findByAutorId(Long id);
    Livro findByTitulo(String titulo);
    List<Livro> findByCategoriaId(Long id);

    @Query("""
    SELECT l FROM Livro l
    WHERE (:nomeAutor IS NULL OR LOWER(l.autor.nome) LIKE LOWER(CONCAT('%', :nomeAutor, '%')))
      AND (:ano IS NULL OR l.anoPublicacao = :ano)
      AND (:idCategoria IS NULL OR l.categoria.id = :idCategoria)
           """)
    List<Livro> findAllByQuery(
            @Param("idCategoria") Integer idCategoria,
            @Param("ano") Integer ano,
            @Param("nomeAutor") String nomeAutor
    );
}