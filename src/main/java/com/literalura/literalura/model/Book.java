package com.literalura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignora propiedades no mapeadas del JSON
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version = 0L; // Inicializa la versión para evitar nulls

    @JsonAlias("title")
    @Column(nullable = false)
    private String titulo;

    @JsonAlias("languages")
    @ElementCollection
    @CollectionTable(name = "book_languages", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "language")
    private List<String> languages;

    @JsonAlias("download_count")
    private int descargas;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Author autor;

    // Constructor sin parámetros (requerido por Hibernate)
    public Book() {
    }

    // Constructor con JsonCreator
    @JsonCreator
    public Book(
            @JsonProperty("id") long id,
            @JsonProperty("title") String titulo,
            @JsonProperty("authors") List<Author> autores, // Recibe una lista de autores
            @JsonProperty("download_count") int descargas,
            @JsonProperty("languages") List<String> languages
    ) {
        this.id = id;
        this.titulo = titulo;
        this.languages = languages;
        this.descargas = descargas;

        if (autores != null && !autores.isEmpty()) {
            this.autor = autores.get(0);
        }
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Author getAutor() {
        return autor;
    }

    public void setAutor(Author autor) {
        this.autor = autor;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public int getDescargas() {
        return descargas;
    }

    public void setDescargas(int descargas) {
        this.descargas = descargas;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor=" + autor +
                ", descargas=" + descargas +
                ", languages=" + languages +
                '}';
    }
}
