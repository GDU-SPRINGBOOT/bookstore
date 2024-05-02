package com.example.bookstorebackend.Domain.CategoriesService;

import com.example.bookstorebackend.Domain.Model.Book.Categories;

import java.util.List;

public interface AdminCategoriesService {
    Categories addCategory(Categories categories);
    List<Categories> getAllCategory();
    Categories findById(Long id);
    Categories updateCategory(Long id, Categories newCategory);
    void removeCategory(Long id);
}
