package com.example.bookstorebackend.Presentation.Controller.BookController;

import com.example.bookstorebackend.Domain.BookService.ClientBookService;
import com.example.bookstorebackend.Domain.Model.Book.Book;
import com.example.bookstorebackend.Domain.Model.Book.Categories;
import com.example.bookstorebackend.Domain.Respones.ResponseObject;
import com.example.bookstorebackend.Domain.Respones.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/client/book")
@RequiredArgsConstructor
public class ClientBookController {
    @Autowired
    private ClientBookService clientBookService;

    @GetMapping("/getAll")
    public ResponseEntity<ResponseObject> getAll() {
        List<Book> books = clientBookService.getAllBook();
        List<Object> response = new ArrayList<>();

        for (Book book : books) {
            Map<String, Object> bookData = new HashMap<>();
            bookData.put("id", book.getId());
            bookData.put("price", book.getPrice());
            bookData.put("image", book.getBookImage());
            bookData.put("name", book.getName());
            bookData.put("author", book.getAuthor());
            bookData.put("description", book.getDescription());

            // Lấy thông tin Categories
            Set<Categories> categories = book.getCategoriesSet();
            List<String> categoriesNames = new ArrayList<>();
            for (Categories category : categories) {
                categoriesNames.add(category.getName());
            }
            bookData.put("categories", categoriesNames);
            response.add(bookData);
        }
        return ResponseUtils.buildSuccessResponse(response, "SUCCESSFULLY");

    }

    @GetMapping("/search")
    public ResponseEntity<ResponseObject> searchBook(@RequestParam(name = "keyword", required = false) String keyword) {
        List<Book> searchResults = clientBookService.searchBooks(keyword);
        if (!searchResults.isEmpty()) {
            return ResponseUtils.buildSuccessResponse(searchResults, "SUCCESSFULLY FOUND BOOK : " + keyword);
        } else {
            return ResponseUtils.buildErrorResponse(HttpStatus.NOT_FOUND, "NOT FOUND BOOK: " + keyword);
        }
    }

    @GetMapping("/finbyId/{id}")
    public ResponseEntity<ResponseObject> findbyId(@PathVariable("id") Long bookid) {
        Optional<Book> book = clientBookService.findById(bookid);
        if (!book.isEmpty()) {
            return ResponseUtils.buildSuccessResponse(book, "SUCCESSFULLY FIND ID BOOK : " + bookid);
        } else {
            return ResponseUtils.buildErrorResponse(HttpStatus.BAD_REQUEST, "BOOK NOT FOUND : " + bookid);
        }

    }


}
