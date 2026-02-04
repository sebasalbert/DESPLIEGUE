package com.daw2.LibraryDAW.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.daw2.LibraryDAW.model.Book;
import com.daw2.LibraryDAW.service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
@CrossOrigin(origins = "*")//(origins = "http://localhost:3000")
@Validated
public class BookController {
    
    private final BookService bookService;
    
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    
    //GET /books - Status 200 OK automático
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }
    
    //GET / books/{id} - Status 200 si existe, 404 si no
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Book not found with id: " + id
                ));
    }
    
    //POST / books - Status 201 CREATED
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book createBook(@Valid @RequestBody Book book) {
        return bookService.createBook(book);
    }
    
    //PUT / books/{id} - Status 200 si éxito, 404 si no existe
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @Valid @RequestBody Book bookDetails) {
        try {
            return bookService.updateBook(id, bookDetails);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                e.getMessage()
            );
        }
    }
    
    //DELETE / books/{id} - Status 204 NO CONTENT
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        /*if (!bookService.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Book not found with id: " + id
            );
        }*/
        bookService.deleteBook(id);
    }
    
    // GET /books/search?title=xxx - Status 200
    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String title) {
        List<Book> books = bookService.searchBooksByTitle(title);
        if (books.isEmpty()) {
            // Status 200 con lista vacía (no 204)
            // Si quieres 204: throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return books;
    }
    
    // GET /books/author/{author} - Status 200
    @GetMapping("/author/{author}")
    public List<Book> getBooksByAuthor(@PathVariable String author) {
        return bookService.getBooksByAuthor(author);
    }
}