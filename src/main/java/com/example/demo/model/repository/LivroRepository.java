package com.example.demo.model.repository;

import com.example.demo.model.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    Optional<Livro> findByIsbn(String isbn);
    List<Livro> findByAutorId(Long id);
}
