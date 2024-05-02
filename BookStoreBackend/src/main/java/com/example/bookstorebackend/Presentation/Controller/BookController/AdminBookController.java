package com.example.bookstorebackend.Presentation.Controller.BookController;


import com.example.bookstorebackend.Domain.BookService.AdminBookService;
import com.example.bookstorebackend.Domain.Model.Book.Book;
import com.example.bookstorebackend.Domain.Model.Book.Categories;
import com.example.bookstorebackend.Domain.Respones.ResponseObject;
import com.example.bookstorebackend.Domain.Respones.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Service
@RequiredArgsConstructor
@RestController
@RequestMapping("api/admin/book")
public class AdminBookController {
    private final AdminBookService adminBookService;

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addBook(@RequestBody Book book) {
        try {
            Book savedBook = adminBookService.addBook(book);
            Map<String, Object> bookData= responseBook(savedBook);
            return ResponseUtils.buildCreatedResponse(bookData, "Book added successfully");
        } catch (Exception e) {
            return ResponseUtils.buildErrorResponse(HttpStatus.BAD_REQUEST, "Failed to add book: " + e.getMessage());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseObject> getAllBooks() {
        List<Book> books = adminBookService.getAllBooks();
        List<Object> response = new ArrayList<>();

        for (Book book : books) {
            Map<String, Object> bookData = responseBook(book);
            response.add(bookData);
        }
        return ResponseUtils.buildSuccessResponse(response, "Successfully retrieved data");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteBook(@PathVariable Long id) {
        try {
            adminBookService.removeBook(id);
            return ResponseUtils.buildSuccessResponse(null, "Book deleted successfully");

        } catch (Exception e) {
            return ResponseUtils.buildErrorResponse(HttpStatus.BAD_REQUEST, "Book not found with id: " + id);
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ResponseObject> updateBook(@PathVariable Long id, @RequestBody Book newBook) {
        try {
            Book updatedBook = adminBookService.updateBook(id, newBook);
            return ResponseUtils.buildSuccessResponse(updatedBook, "Book updated successfully");
        } catch (Exception e) {
            return ResponseUtils.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update book: " + e.getMessage());
        }
    }

    @GetMapping("/findById/{bookId}")
    public ResponseEntity<ResponseObject> findById(@PathVariable Long bookId) {
        Optional<Book> book = adminBookService.findById(bookId);
        Map<String, Object> bookData = new HashMap<>();

        if (book.isPresent()) {
            Book newBook = book.get();
            bookData = responseBook(newBook);

            return ResponseUtils.buildSuccessResponse(bookData, "Successfully retrieved data");
        } else {

            return ResponseUtils.buildErrorResponse(HttpStatus.BAD_REQUEST, "book not found");
        }
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<ResponseObject> searchBooks(@PathVariable String keyword) {
        List<Book> books = adminBookService.searchBooks(keyword);
        if (books.isEmpty()) {
            return ResponseUtils.buildErrorResponse(HttpStatus.BAD_REQUEST, "No books found for keyword: " + keyword);
        } else {
            List<Object> response = new ArrayList<>();

            for (Book book : books) {
                Map<String, Object> bookData = responseBook(book);
                response.add(bookData);
            }
            return ResponseUtils.buildSuccessResponse(response, "Books found for keyword: " + keyword);
        }
    }

    private Map<String, Object> responseBook(Book book) {
        Map<String, Object> bookData = new HashMap<>();
        bookData.put("id", book.getId());
        bookData.put("price", book.getPrice());
        bookData.put("bookImage", book.getBookImage());
        bookData.put("name", book.getName());
        bookData.put("author", book.getAuthor());
        bookData.put("description", book.getDescription());

        // Lấy thông tin Categories
        Set<Categories> categories = book.getCategoriesSet();
        List<Map<String, Object>> categoriesData = new ArrayList<>();
        for (Categories category : categories) {
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("name", category.getName());
            categoryMap.put("id", category.getCategoriesID());
            categoriesData.add(categoryMap);
        }
        bookData.put("categories", categoriesData);

        return bookData;
    }

}
