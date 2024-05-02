package com.example.bookstorebackend.Persistence.DAO;

import com.example.bookstorebackend.Domain.Model.Book.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    Optional<Categories> findByName(String name);
    Optional<Categories> findByCategoriesID(Long id);
}
