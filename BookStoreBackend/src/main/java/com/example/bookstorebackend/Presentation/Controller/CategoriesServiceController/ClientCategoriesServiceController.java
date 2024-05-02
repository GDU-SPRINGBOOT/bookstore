package com.example.bookstorebackend.Presentation.Controller.CategoriesServiceController;

import com.example.bookstorebackend.Domain.CategoriesService.ClientCategoriesService;
import com.example.bookstorebackend.Domain.Model.Book.Categories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/client/categories")
public class ClientCategoriesServiceController {
    private final ClientCategoriesService clientCategoriesService;

    @Autowired
    public ClientCategoriesServiceController(ClientCategoriesService clientCategoriesService) {
        this.clientCategoriesService = clientCategoriesService;
    }

    @GetMapping
    public List<Categories> getAllCategory(){
        return clientCategoriesService.getAllCategories();
    }
}
