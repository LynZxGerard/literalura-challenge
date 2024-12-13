# LiterAlura

<p align="center">
  
  ![Java Badge](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
  ![Spring Badge](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
  ![PostgreSQL Badge](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
</p>

![BannerLiteralura](https://github.com/user-attachments/assets/e7d52d9c-f2e7-4739-aa5f-891d1c5dd370)



---

## ✉ **Índice**

1. [Tecnologías Utilizadas](#tecnolog%C3%ADas-utilizadas)
2. [Descripción del Proyecto](#descripción-del-proyecto)
3. [Estructura del proyecto](#estructura-del-proyecto)
4. [Demostración de Funciones y Aplicaciones](#demostración-de-funciones-y-aplicaciones)
5. [Acceso al Proyecto](#acceso-al-proyecto)

---

## Tecnologías Utilizadas

- **Lenguaje:** Java 21
- **Framework:** Spring Boot
- **Base de Datos:** PostgreSQL
- **Manejo de Dependencias:** Maven
- **Librerías Adicionales:**
  - Spring Data JPA
  - Jackson para serialización JSON
  - PostgreSQL Driver
  
<br>

---

<br>

## Descripción del Proyecto

**LiterAlura** es un sistema interactivo desarrollado en Java que permite gestionar libros y autores mediante consultas avanzadas a una base de datos PostgreSQL. El proyecto consume la API de **Gutendex** para obtener datos sobre libros de dominio público, proporcionando información detallada sobre títulos, autores, temas, idiomas y enlaces de descarga.

El sistema incluye funciones como listar libros, filtrar por idioma, identificar autores vivos en un año específico y obtener el top 3 de libros más descargados. Además, se utiliza el framework **Spring** para gestionar la lógica del backend y la persistencia de datos. Para la persistencia, se utiliza **PostgreSQL** junto con **JPA** (Java Persistence API) para interactuar con la base de datos de manera eficiente y estructurada. **Jackson** se emplea para manejar el consumo de datos de la API de Gutendex y convertir los datos JSON a objetos Java, facilitando la integración con los servicios externos.

<br>

---

<br>

## Estructura del Proyecto

El proyecto está dividido en varios paquetes para mantener una arquitectura limpia y escalable:

- **`com.proyecto.model`**: Contiene las clases `Author.java` y `Book.java`, asi como `BookResponse.java` que representan los modelos de datos del proyecto.
- **`com.proyecto.principal`**: Contiene la clase `Principal.java`, que maneja la interacción con el usuario y coordina las operaciones del sistema.
- **`com.proyecto.repository`**: Contiene la clase `RepositoryLibro.java` y `RepositoryAuthor.java`, que maneja las consultas a la base de datos y la persistencia de los libros.
- **`com.proyecto.service`**: Contiene las clases `ConsumoAPI.java`, `ConversionDatos.java` y la interfaz `IConversionDatos.java`, encargadas de consumir la API de Gutendex, convertir los datos y realizar operaciones relacionadas con la manipulación de los libros y autores.

Con esta estructura modular, el proyecto es fácil de extender y mantener a medida que se añaden nuevas funcionalidades o se realizan mejoras en el sistema.

<br>

---

<br>

## Demostración de Funciones y Aplicaciones

<p align="center">
  <img src="https://github.com/user-attachments/assets/0a2c9f41-96c7-4a8b-b7c6-952e611de0c5" alt="MenuPrincipalLiteralura">
</p>

1. **Buscar libro por título**  
  Permite buscar un libro específico ingresando su título exacto.
---
2. **Listar todos los libros**  
  Muestra una lista completa de todos los libros almacenados en la base de datos.
---
3. **Filtrar libros por idioma**  
  Filtra y lista los libros disponibles en un idioma específico.

<p align="center">
  <img src="https://github.com/user-attachments/assets/0d3f18e6-7741-4e63-8106-8aa733b257b3" alt="FiltrarIdioma">
</p>

---

4. **Listar autores de los libros buscados**  
  Muestra los autores de los libros que se han buscado previamente.
---
5. **Listar autores vivos en un año específico**  
  Genera un listado de autores que estaban vivos durante un año seleccionado.

<p align="center">
  <img src="https://github.com/user-attachments/assets/18499fff-593b-4c92-a62f-5f8477d2b913" alt="Autores vivos en X año">
</p>

---
6. **Buscar cantidad de libros en un idioma**  
  Muestra cuántos libros hay disponibles en un idioma específico.
---
7. **Mostrar Top 3 de tus libros más descargados**  
  Presenta los tres libros con mayor número de descargas, ordenados de mayor a menor.

<p align="center">
  <img src="https://github.com/user-attachments/assets/b58b973f-09b8-4f58-83e4-7a9cb42dce25" alt="Top 3 libros">
</p>

---

8. **Buscar un autor por nombre**  
  Permite buscar un autor ingresando su nombre y muestra sus datos (nombre, año de nacimiento y muerte).
---
9. **Salir**  
  Finaliza la ejecución del programa.


<br>

---

<br>

## Acceso al Proyecto

1. **Clonar el Repositorio**
   ```bash
   git clone https://github.com/LynZxGerard/literalura-challenge
   ```
2. **Configurar la Base de Datos:**
   - Asegúrate de tener PostgreSQL instalado.
   - Configura las credenciales de conexión con tu propia base de datos en el archivo `application.properties`. Debes cambiar las variables por las direcciones y credenciales correspondientes a tu base de datos. Aquí tienes un ejemplo de configuración:
   ```properties
   spring.application.name=literalura
   spring.datasource.url=jdbc:postgresql://${DB_HOST}/challenge_literalura
   spring.datasource.username=${DB_USER}
   spring.datasource.password=${DB_PASSWORD}
3. **Ejecución del Proyecto:**
   ```bash
   mvn spring-boot:run
   ```

---


