package com.example.bookstorebackend.Domain.BookService;

import com.example.bookstorebackend.Domain.Model.Book.Book;
import com.example.bookstorebackend.Domain.Model.Book.Categories;
import com.example.bookstorebackend.Persistence.DAO.BookRepository;
import com.example.bookstorebackend.Persistence.DAO.CategoriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminBookServiceImp implements AdminBookService {

    private final BookRepository bookRepository;

    private final CategoriesRepository categoryRepository;
    @Override
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Book saveBook(Book book) {
        return addCategoryToBook(book, book);
    }
    @Override
    public Book addBook(Book book) {
       return  saveBook(book);
    }

    public Book updateBook(Long id, Book newBook) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            Book existingBook = bookOptional.get();
            BeanUtils.copyProperties(newBook, existingBook, "id", "categoriesSet");

            return addCategoryToBook(newBook, existingBook);
        } else {
            throw new RuntimeException("Book not found with id: " + id);
        }
    }

    private Book addCategoryToBook(Book newBook, Book existingBook) {
        Set<Categories> savedCategories = new HashSet<>();
        for (Categories category : newBook.getCategoriesSet()) {
            Optional<Categories> existingCategory = categoryRepository.findById(category.getCategoriesID());
            if (existingCategory.isPresent()) {
                savedCategories.add(existingCategory.get());
            } else {
                Categories savedCategory = categoryRepository.save(category);
                savedCategories.add(savedCategory);
            }
        }
        existingBook.setCategoriesSet(savedCategories);

        return bookRepository.save(existingBook);
    }


    @Override
    public void removeBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();

    }

    @Override
    public List<Book> searchBooks(String keyword) {
        return bookRepository.findByNameContainingIgnoreCase(keyword);
    }
}
