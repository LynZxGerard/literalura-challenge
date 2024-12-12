package com.literalura.literalura.repository;

import com.literalura.literalura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RepositoryAuthor extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Author a WHERE a.anioNacimiento <= :anio AND (a.anioMuerte IS NULL OR a.anioMuerte >= :anio)")
    List<Author> findAuthorsAliveInYear(@Param("anio") int anio);

    List<Author> findByNombreContainingIgnoreCase(String nombre);
}