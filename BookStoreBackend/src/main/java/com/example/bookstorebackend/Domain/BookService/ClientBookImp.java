package com.example.bookstorebackend.Domain.BookService;

import com.example.bookstorebackend.Domain.Model.Book.Book;
import com.example.bookstorebackend.Persistence.DAO.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientBookImp implements ClientBookService{
    @Autowired
    private BookRepository bookRepository;


    @Override
    public List<Book> getAllBook() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> searchBooks(String keyword) {
        return bookRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }
}
