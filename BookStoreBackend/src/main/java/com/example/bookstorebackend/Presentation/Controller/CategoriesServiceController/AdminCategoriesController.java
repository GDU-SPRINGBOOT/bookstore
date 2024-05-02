package com.example.bookstorebackend.Presentation.Controller.CategoriesServiceController;

import com.example.bookstorebackend.Domain.CategoriesService.AdminCategoriesService;
import com.example.bookstorebackend.Domain.Model.Book.Categories;
import com.example.bookstorebackend.Domain.Respones.ResponseObject;
import com.example.bookstorebackend.Domain.Respones.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoriesController {
    private final AdminCategoriesService adminCategoriesService;

    @Autowired
    public AdminCategoriesController(AdminCategoriesService adminCategoriesService) {
        this.adminCategoriesService = adminCategoriesService;
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getAllCategories() {
        try {
            List<Categories> categories = adminCategoriesService.getAllCategory();
            return ResponseUtils.buildSuccessResponse(categories, "Successfully retrieved data");
        } catch (Exception e) {
            return ResponseUtils.buildErrorResponse(HttpStatus.NOT_FOUND,"Failed to retrieve data: " + e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getCategoryById(@PathVariable Long id) {
        try {
            Categories categories = adminCategoriesService.findById(id);
            return ResponseUtils.buildSuccessResponse(categories, "Successfully retrieved data");
        } catch (Exception e) {
            return ResponseUtils.buildErrorResponse(HttpStatus.NOT_FOUND,"Failed to retrieve data: " + e.getMessage());
        }
    }
    @PostMapping
    public ResponseEntity<ResponseObject> addCategory(@RequestBody Categories categories) {
        try {
            Categories savedCategory = adminCategoriesService.addCategory(categories);
            return ResponseUtils.buildCreatedResponse(savedCategory, "Category added successfully");
        } catch (Exception e) {
            return ResponseUtils.buildErrorResponse(HttpStatus.BAD_REQUEST, "Failed to add category: " + e.getMessage());
        }
    }
    @PatchMapping("/update")
    public ResponseEntity<ResponseObject> updateCategory(@RequestParam Long id, @RequestBody Categories categories) {
        try {
            Categories updatedCategory = adminCategoriesService.updateCategory(id, categories);
            return ResponseUtils.buildSuccessResponse(updatedCategory, "Category updated successfully");
        } catch (Exception e) {
            return ResponseUtils.buildErrorResponse(HttpStatus.BAD_REQUEST, "Failed to update category: " + e.getMessage());
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseObject> deleteCategory(@RequestParam Long id) {
        try {
            adminCategoriesService.removeCategory(id);
            return ResponseUtils.buildSuccessResponse(null, "Category deleted successfully");
        } catch (Exception e) {
            return ResponseUtils.buildErrorResponse(HttpStatus.BAD_REQUEST, "Failed to delete category: " + e.getMessage());
        }
    }
}