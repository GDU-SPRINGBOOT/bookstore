package com.example.bookstorebackend.Domain.CategoriesService;

import com.example.bookstorebackend.Domain.Model.Book.Categories;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClientCategoriesService {
    List<Categories> getAllCategories();
}
