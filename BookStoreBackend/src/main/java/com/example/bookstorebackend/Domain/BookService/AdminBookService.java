package com.example.bookstorebackend.Domain.BookService;

import com.example.bookstorebackend.Domain.Model.Book.Book;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface AdminBookService {
    Optional<Book> findById(Long id);

    Book updateBook(Long id, Book newBook);

    Book addBook(Book book);

    void removeBook(Long id);

    List<Book> searchBooks(String keyword);
    List<Book> getAllBooks();

}
