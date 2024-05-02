package com.example.bookstorebackend.Domain.CategoriesService;

import com.example.bookstorebackend.Domain.Model.Book.Categories;
import com.example.bookstorebackend.Persistence.DAO.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminCategoriesServiceImp implements AdminCategoriesService {
    @Autowired
    private CategoriesRepository categoriesRepository;

    @Override
    public Categories addCategory(Categories categories) {
        return categoriesRepository.save(categories);
    }

    @Override
    public List<Categories> getAllCategory() {
        return categoriesRepository.findAll();
    }

    @Override
    public Categories findById(Long id) {
        Categories categories = categoriesRepository.findById(id).orElse(null);
        if (categories == null) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        return categories;
    }

    @Override
    public Categories updateCategory(Long id, Categories newCategory) {
        Optional<Categories> categoriesOptional = categoriesRepository.findById(id);
        if (categoriesOptional.isPresent()) {
            Categories existingCategory = categoriesOptional.get();
            existingCategory.setName(newCategory.getName());
            return categoriesRepository.save(existingCategory);
        } else {
            throw new RuntimeException("Category not found with id: " + id);
        }
    }
    @Override
    public void removeCategory(Long id) {
        Categories categories = categoriesRepository.findById(id).orElse(null);
        if (categories == null) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoriesRepository.deleteById(id);
    }
}
