package com.daw2.LibraryDAW.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daw2.LibraryDAW.model.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    List<Book> findByAuthor(String author);
    
    List<Book> findByGenre(String genre);
    
    List<Book> findByYear(Integer year);
    
    List<Book> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT b FROM Book b WHERE b.year BETWEEN :startYear AND :endYear")
    List<Book> findBooksByYearRange(@Param("startYear") Integer startYear, 
                                     @Param("endYear") Integer endYear);
    
    @Query("SELECT b FROM Book b WHERE LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))")
    List<Book> findBooksByAuthorContainingIgnoreCase(@Param("author") String author);
}
