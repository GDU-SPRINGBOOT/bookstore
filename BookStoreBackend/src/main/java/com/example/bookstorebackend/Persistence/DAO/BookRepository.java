package com.example.bookstorebackend.Persistence.DAO;

import com.example.bookstorebackend.Domain.Model.Book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByName(String name);

    Optional<Book> findById(Long id);

    List<Book> findByNameContainingIgnoreCase(String keyword);
}
