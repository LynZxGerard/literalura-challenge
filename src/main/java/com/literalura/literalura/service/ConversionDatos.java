package com.literalura.literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.literalura.literalura.model.Book;
import com.literalura.literalura.model.BookResponse;
import com.literalura.literalura.model.Author; // Asegúrate de importar Author
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ConversionDatos {
    private final ObjectMapper objectMapper;

    public ConversionDatos() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Convierte un JSON recibido desde la API en una lista de objetos Book.
     *
     * @param json El JSON recibido.
     * @return Una lista de objetos Book.
     */
    public List<Book> convertirJsonALibros(String json) {
        try {
            BookResponse booksResponse = objectMapper.readValue(json, BookResponse.class);
            return booksResponse.getResults().stream()
                    .map(libro -> {
                        // Envolvemos el autor en una lista, incluso si solo es uno
                        Author autor = libro.getAutor(); // Obtener el primer autor
                        return new Book(
                                libro.getId(),
                                libro.getTitulo(),
                                autor != null ? List.of(autor) : Collections.emptyList(), // Pasamos el autor como una lista
                                libro.getDescargas(),
                                libro.getLanguages().isEmpty() ? List.of("Desconocido") : libro.getLanguages()
                        );
                    })
                    .toList();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir JSON a lista de libros", e);
        }
    }


    /**
     * Procesa el JSON recibido y devuelve el primer libro encontrado.
     *
     * @param json El JSON recibido de la API.
     * @return El primer libro encontrado o null si no hay resultados.
     */
    public Book procesarPrimerLibro(String json) {
        List<Book> libros = convertirJsonALibros(json);
        return libros.stream()
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("No se encontraron libros con ese título.");
                    return null;
                });
    }
}
