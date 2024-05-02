package com.example.bookstorebackend.Domain.CategoriesService;

import com.example.bookstorebackend.Domain.Model.Book.Categories;
import com.example.bookstorebackend.Persistence.DAO.CategoriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientCategoriesServiceImp implements ClientCategoriesService {
    @Autowired
    private CategoriesRepository categoriesRepository;
    @Override
    public List<Categories> getAllCategories() {
        return categoriesRepository.findAll();
    }
}
