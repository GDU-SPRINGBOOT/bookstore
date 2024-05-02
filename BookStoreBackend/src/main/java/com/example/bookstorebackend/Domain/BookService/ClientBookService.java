package com.example.bookstorebackend.Domain.BookService;

import com.example.bookstorebackend.Domain.Model.Book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ClientBookService {


    List<Book> getAllBook();

    List<Book> searchBooks(String keyword);

    Optional<Book> findById(Long id);
}

