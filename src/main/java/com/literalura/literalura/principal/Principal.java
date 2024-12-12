package com.literalura.literalura.principal;

import com.literalura.literalura.model.Author;
import com.literalura.literalura.model.Book;
import com.literalura.literalura.service.ConsumoAPI;
import com.literalura.literalura.service.ConversionDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.literalura.literalura.repository.RepositoryLibro;
import com.literalura.literalura.repository.RepositoryAuthor;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

@Component
public class Principal implements CommandLineRunner {

    // INYECCIONES

    @Autowired
    private ConsumoAPI consumoAPI;

    @Autowired
    private ConversionDatos conversionDatos;

    @Autowired
    private RepositoryLibro repositoryLibro;

    @Autowired
    private RepositoryAuthor repositoryAuthor;

    public static void mostrarMenu() {
        System.out.println();
        System.out.println();
        System.out.println("+===================================================+");
        System.out.println("|      _    _ _                _                    |");
        System.out.println("|     | |  (_) |_ ___ _ _ __ _| |_  _ _ _ __ _      |");
        System.out.println("|     | |__| |  _/ -_) '_/ _` | | || | '_/ _` |     |");
        System.out.println("|     |____|_|\\__\\___|_| \\__,_|_|\\_,_|_| \\__,_|     |");
        System.out.println("|                                                   |");
        System.out.println("+===================================================+");
        System.out.println("| 1. Buscar libro por título                        |");
        System.out.println("| 2. Listar todos los libros                        |");
        System.out.println("| 3. Filtrar libros por idioma                      |");
        System.out.println("| 4. Listar autores de los libros buscados          |");
        System.out.println("| 5. Listar autores vivos en un año específico      |");
        System.out.println("| 6. Buscar cantidad de libros en un idioma         |");
        System.out.println("| 7. Mostrar Top 3 de tus libros más descargados    |");
        System.out.println("| 8. Buscar un autor por nombre                     |");
        System.out.println("| 9. Salir                                          |");
        System.out.println("+ ==================================================+");
        System.out.print("Por favor, selecciona una opción -> ");
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            mostrarMenu();

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.println("Por favor, ingresa el término de búsqueda:");
                    String termino = scanner.nextLine().replace(" ", "+");
                    String url = "https://gutendex.com/books?search=" + termino;
                    String json = consumoAPI.obtenerDatos(url);

                    // Procesar el primer libro obtenido del JSON
                    Book libro = conversionDatos.procesarPrimerLibro(json);

                    if (libro != null) {
                        try {

                            if (libro.getAutor() == null) { // Por si hay algun libro que no tenga autor en Gutendex
                                System.out.println("No se encontró autor asociado.");
                            }

                            // Verificar si el libro ya existe en la base de datos
                            Book libroExistente = repositoryLibro.findByTituloAndAutor_Nombre(libro.getTitulo(), libro.getAutor().getNombre());

                            if (libroExistente != null) {
                                // Si el libro ya existe, no hay que guardarlo
                                System.out.println("El libro '" + libro.getTitulo() + "' ya está guardado en tu biblioteca.");
                            } else {
                                // Si el autor no está en la base de datos, lo guardamos
                                Author autor = libro.getAutor();
                                if (autor != null) {
                                    // Verificar si el autor ya existe, y si no, guardarlo
                                    Author autorGuardado = repositoryAuthor.save(autor);

                                    // Ahora se asocia el autor guardado con el libro
                                    libro.setAutor(autorGuardado);
                                }

                                // Limpiar el ID para que Hibernate lo genere automáticamente
                                // Esto sirve porque se necesita ignorar el ID que retorna el JSON
                                libro.setId(null); // Para asegurar que sea un ID único

                                // Guardar el libro
                                System.out.println("Guardando el libro...");
                                Book libroGuardado = repositoryLibro.save(libro);
                                System.out.println("Libro guardado exitosamente en la base de datos.");
                            }

                        } catch (Exception e) {
                            System.out.println("Error al guardar el libro: " + e.getMessage());
                            e.printStackTrace();  // Imprimir el stack trace para ver más detalles del error
                        }
                    } else {
                        System.out.println("No se encontró ningún libro con el término proporcionado.");
                    }
                    break;


                case 2:
                    System.out.println();
                    System.out.println("+=============================================+");
                    System.out.println("|    Listado de todos los libros guardados    |");
                    System.out.println("+=============================================+");
                    System.out.println();
                    System.out.println("===============================================");

                    List<Book> libros = repositoryLibro.findAll();  // Obtiene todos los libros desde la base de datos
                    if (libros.isEmpty()) {
                        System.out.println("No hay libros guardados en la base de datos.");
                    } else {
                        // Imprimir cada libro en el listado
                        libros.forEach(x -> {
                            // Si quieres mostrar información más específica, puedes formatearla
                            System.out.println("ID: " + x.getId());
                            System.out.println("   Título: " + x.getTitulo());
                            System.out.println("   Autor: " + (x.getAutor() != null ? x.getAutor().getNombre() : "No disponible"));

                            // Se tiene que obtener el atributo idiomas asi porque se uso un "ElementCollection" para mapear idiomas
                            // entonces se necesita acceder a una tabla aparte
                            List<String> idiomas = repositoryLibro.findLanguagesByBookId(x.getId());
                            System.out.println("   Idiomas: " + (idiomas.isEmpty() ? "No disponibles" : String.join(", ", idiomas)));

                            System.out.println("   Descargas: " + x.getDescargas());
                            System.out.println("===============================================");
                        });
                    }
                    break;



                case 3:
                    System.out.println();
                    System.out.println("Ingrese el idioma para filtrar (ejemplo: es, en, fr):");
                    String idioma = scanner.nextLine().toLowerCase();  // Por si pone mayusculas el usuario
                    List<Book> librosFiltrados = repositoryLibro.findBooksByLanguage(idioma);

                    if (librosFiltrados.isEmpty()) {
                        System.out.println("No hay libros en ese idioma.");
                    } else {
                        System.out.println("+==============================================+");
                        System.out.println("|     Libros encontrados en el idioma '" + idioma + "'     |");
                        System.out.println("+==============================================+");
                        librosFiltrados.forEach(x -> {
                            System.out.println("ID: " + x.getId());
                            System.out.println("   Título: " + x.getTitulo());
                            System.out.println("   Autor: " + (x.getAutor() != null ? x.getAutor().getNombre() : "No disponible"));

                            // Se tiene que obtener el atributo idiomas asi porque se uso un "ElementCollection" para mapear idiomas
                            // entonces se necesita acceder a una tabla aparte
                            List<String> idiomas = repositoryLibro.findLanguagesByBookId(x.getId());

                            System.out.println("   Idiomas: " + (idiomas.isEmpty() ? "No disponibles" : String.join(", ", idiomas)));

                            System.out.println("   Descargas: " + x.getDescargas());
                            System.out.println("===============================================");
                        });
                    }
                    break;

                case 4:
                    List<Author> autores = repositoryAuthor.findAll();  // Llamar a todos los autores
                    if (autores.isEmpty()) {
                        System.out.println("No se han encontrado autores.");
                    } else {
                        System.out.println("+==============================================+");
                        System.out.println("|              Listado de autores              |");
                        System.out.println("+==============================================+");
                        Set<String> seenAuthors = new HashSet<>();
                        autores.forEach(autor -> {
                            String autorKey = autor.getNombre() + autor.getAnioNacimiento() + autor.getAnioMuerte();
                            if (!seenAuthors.contains(autorKey)) {
                                seenAuthors.add(autorKey);
                                System.out.println("Nombre: " + autor.getNombre());
                                System.out.println("Año de nacimiento: " + autor.getAnioNacimiento());
                                System.out.println("Año de muerte: " + (autor.getAnioMuerte() != null ? autor.getAnioMuerte() : "Aún vivo"));
                                System.out.println("===============================================");
                            }
                        });
                    }
                    break;



                case 5:
                    System.out.println();
                    System.out.println("Ingrese el año:");
                    int anio = scanner.nextInt();


                    List<Author> autoresVivos = repositoryAuthor.findAuthorsAliveInYear(anio);
                    if (autoresVivos.isEmpty()) {
                        System.out.println("No hay autores vivos en el año " + anio + ".");
                    } else {
                        System.out.println("+===============================================+");
                        System.out.println("|           Autores vivos en el año " + anio + "        |");
                        System.out.println("+===============================================+");
                        autoresVivos.forEach(autor -> {
                            System.out.println("Nombre: " + autor.getNombre());
                            System.out.println("Año de nacimiento: " + autor.getAnioNacimiento());
                            System.out.println("Año de muerte: " + (autor.getAnioMuerte() != null ? autor.getAnioMuerte() : "Aún vivo"));
                            System.out.println("===============================================");
                        });
                    }
                    break;


                case 6:
                    System.out.println();
                    System.out.println("Selecciona el idioma para ver la cantidad de libros:");
                    System.out.println("1. Español (ES)");
                    System.out.println("2. Alemán (DE)");
                    System.out.println("3. Francés (FR)");

                    int idiomaSeleccionado = scanner.nextInt();
                    scanner.nextLine();

                    String idiomaBusqueda = "";

                    switch (idiomaSeleccionado) {
                        case 1:
                            idiomaBusqueda = "es";  // Español

                            break;
                        case 2:
                            idiomaBusqueda = "de";  // Alemán
                            break;
                        case 3:
                            idiomaBusqueda = "fr";  // Francés
                            break;
                        default:
                            System.out.println("Opción no válida.");
                            return;
                    }

                    long cantidadLibros = repositoryLibro.countByLanguagesContains(idiomaBusqueda);
                    idiomaBusqueda = idiomaBusqueda.toUpperCase();

                    System.out.println();
                    System.out.println("Cantidad de libros en el idioma '" + idiomaBusqueda + "' = " + cantidadLibros);
                    break;

                case 7:
                    List<Book> top3Libros = repositoryLibro.findTop3BooksByDescargasNative();

                    if (top3Libros.isEmpty()) {
                        System.out.println("No hay libros disponibles.");
                    } else {
                        System.out.println();
                        System.out.println("+================================================+");
                        System.out.println("|          Top 3 libros más descargados          |");
                        System.out.println("+================================================+");
                        int rank = 1;
                        for (Book y : top3Libros) {
                            System.out.println("Rank " + rank + ":");
                            System.out.println("Título: " + y.getTitulo());
                            System.out.println("Autor: " + (y.getAutor() != null ? y.getAutor().getNombre() : "No disponible"));
                            System.out.println("Descargas: " + y.getDescargas());
                            System.out.println("===============================================");
                            rank++;
                        }
                    }
                    break;

                case 8:
                    System.out.println("Ingrese el nombre del autor para buscar:");
                    String nombreAutor = scanner.nextLine().toLowerCase();
                    List<Author> autoresFiltrados = repositoryAuthor.findByNombreContainingIgnoreCase(nombreAutor);

                    if (autoresFiltrados.isEmpty()) {
                        System.out.println("No se encontraron autores con el nombre '" + nombreAutor + "'.");
                    } else {
                        System.out.println("+=========================================================+");
                        System.out.println("|       Autores encontrados con el nombre '" + nombreAutor + "':");
                        System.out.println("+=========================================================+");
                        autoresFiltrados.stream()
                                .findFirst()  // Tomar solo el primer autor coincidente
                                .ifPresent(autor -> {
                                    System.out.println("Nombre: " + autor.getNombre());
                                    System.out.println("Año de nacimiento: " + autor.getAnioNacimiento());
                                    System.out.println("Año de muerte: " + (autor.getAnioMuerte() != null ? autor.getAnioMuerte() : "N/A"));
                                    System.out.println("===============================================");
                                });
                    }
                    break;

                case 9:
                    System.out.println();
                    System.out.println();
                    System.out.println("  _  _   _   ___ _____ _     ___ ___  ___  _  _ _____ ___  ");
                    System.out.println(" | || | /_\\ / __|_   _/_\\   | _ | _ \\/ _ \\| \\| |_   _/ _ \\ ");
                    System.out.println(" | __ |/ _ \\\\__ \\ | |/ _ \\  |  _|   | (_) | .` | | || (_) |");
                    System.out.println(" |_||_/_/ \\_|___/ |_/_/ \\_\\ |_| |_|_\\\\___/|_|\\_| |_| \\___/ ");
                    System.out.println("                                                           ");

                    return;

                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        }
    }

}
