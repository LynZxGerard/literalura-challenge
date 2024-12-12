package com.literalura.literalura.repository;

import com.literalura.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryLibro extends JpaRepository<Book, Integer> {

    Book findByTituloAndAutor_Nombre(String titulo, String autorNombre);
    long countByLanguagesContains(String idioma);

    @Query(value = "SELECT bl.language FROM book_languages bl WHERE bl.book_id = :bookId", nativeQuery = true)
    List<String> findLanguagesByBookId(@Param("bookId") Long bookId);

    @Query(value = "SELECT b.* FROM book b " +
            "JOIN book_languages bl ON b.id = bl.book_id " +
            "WHERE bl.language = ?1", nativeQuery = true)
    List<Book> findBooksByLanguage(String idioma);

    @Query(value = "SELECT * FROM book ORDER BY descargas DESC LIMIT 3", nativeQuery = true)
    List<Book> findTop3BooksByDescargasNative();
}
