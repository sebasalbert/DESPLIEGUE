package com.daw2.LibraryDAW.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.daw2.LibraryDAW.model.Book;
import com.daw2.LibraryDAW.repository.BookRepository;

class BookServiceTest {
	

    @Mock
    private BookRepository bookRepository;
    
    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        // Inicializa los mocks e inyecta las dependencias
        MockitoAnnotations.openMocks(this);
    }
    

    @Test
    void createBook_WhenBookDoesNotExist_ShouldSaveBook() {
        // Arrange
        Book newBook = new Book(null, "El Quijote", "Cervantes", 1605, 
                               "Novela", 863, "Historia de un hidalgo...");
        
        // Simulamos que no existe ningún libro con ese título y autor
        when(bookRepository.findByTitleAndAuthor("El Quijote", "Cervantes"))
            .thenReturn(new ArrayList<>());
        
        Book savedBook = new Book(1L, "El Quijote", "Cervantes", 1605, 
                                 "Novela", 863, "Historia de un hidalgo...");
        when(bookRepository.save(newBook)).thenReturn(savedBook);

        // Act
        Book result = bookService.createBook(newBook);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("El Quijote", result.getTitle());
        verify(bookRepository).save(newBook);
    }

    @Test
    void createBook_WhenBookAlreadyExists_ShouldReturnNull() {
        // Arrange
        Book newBook = new Book(null, "Cien años de soledad", "García Márquez", 
                               1967, "Realismo mágico", 471, "Historia de Macondo...");
        
        Book existingBook = new Book(1L, "Cien años de soledad", "García Márquez", 
                                    1967, "Realismo mágico", 471, "Historia de Macondo...");
        
        // Simulamos que ya existe un libro con ese título y autor
        when(bookRepository.findByTitleAndAuthor("Cien años de soledad", "García Márquez"))
            .thenReturn(Arrays.asList(existingBook));

        // Act
        Book result = bookService.createBook(newBook);

        // Assert
        assertNull(result);
        verify(bookRepository, never()).save(newBook);
    }

    @Test
    void createBook_ShouldCheckForDuplicateBeforeSaving() {
        // Arrange
        Book newBook = new Book(null, "Rayuela", "Cortázar", 1963, 
                               "Experimental", 736, "Novela innovadora...");
        
        when(bookRepository.findByTitleAndAuthor("Rayuela", "Cortázar"))
            .thenReturn(new ArrayList<>());
        
        Book savedBook = new Book(2L, "Rayuela", "Cortázar", 1963, 
                                 "Experimental", 736, "Novela innovadora...");
        when(bookRepository.save(newBook)).thenReturn(savedBook);

        // Act
        Book result = bookService.createBook(newBook);

        // Assert - Verificamos que primero se consulta y luego se guarda
        verify(bookRepository).findByTitleAndAuthor("Rayuela", "Cortázar");
        verify(bookRepository).save(newBook);
        assertNotNull(result);
        assertEquals(2L, result.getId());
    }

}
