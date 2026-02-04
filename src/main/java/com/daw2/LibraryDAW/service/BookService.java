package com.daw2.LibraryDAW.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw2.LibraryDAW.model.Book;
import com.daw2.LibraryDAW.repository.BookRepository;

@Service
@Transactional
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }
    
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }
    
    public Book updateBook(Long id, Book bookDetails) {
        return bookRepository.findById(id).map(book -> {
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setYear(bookDetails.getYear());
            book.setGenre(bookDetails.getGenre());
            book.setPages(bookDetails.getPages());
            book.setDescription(bookDetails.getDescription());
            return bookRepository.save(book);
        }).orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id));
    }
    
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
    
    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }
    
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }
    
    public List<Book> getBooksByGenre(String genre) {
        return bookRepository.findByGenre(genre);
    }
}