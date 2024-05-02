package com.example.bookstorebackend;

import com.example.bookstorebackend.Domain.Model.Book.Book;
import com.example.bookstorebackend.Domain.Model.Book.Categories;
import com.example.bookstorebackend.Domain.Model.Cart.Cart;
import com.example.bookstorebackend.Domain.Model.Cart.CartItem;
import com.example.bookstorebackend.Domain.Model.Order.Order;
import com.example.bookstorebackend.Domain.Model.Order.OrderStatus;
import com.example.bookstorebackend.Domain.Model.User.User;
import com.example.bookstorebackend.Domain.Security.ApplicationConfig;
import com.example.bookstorebackend.Persistence.DAO.BookRepository;
import com.example.bookstorebackend.Persistence.DAO.CategoriesRepository;
import com.example.bookstorebackend.Persistence.DAO.OrderdetailRepository;
import com.example.bookstorebackend.Persistence.DAO.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.*;
@Slf4j
@SpringBootApplication
public class BookStoreBackendApplication {
    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private OrderdetailRepository orderdetailRepository;
    private final List<Book> bookList = new ArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(BookStoreBackendApplication.class, args);
    }
    @Autowired


    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, BookRepository bookRepository) {
        return args -> {
            User Phuc = new User(null, "Phuc", "Tran"
                    , "tthanhphuc753@gmail.com"
                    , applicationConfig.passwordEncoder().encode("123456")
                    , "ADMIN", null, true, null, null);
            userRepository.save(Phuc);

            User Hoang = new User(null, "Hoang", "Nguyen",
                    "nguyenhoang@gmail.com",
                    applicationConfig.passwordEncoder().encode("123456"),
                    "USER", null, true, null, null);
            userRepository.save(Hoang);

            List<Categories> lc = new ArrayList<>();
            Categories newCat = new Categories(1, "Document", null);
            lc.add(newCat);
            Categories newCat1 = new Categories(2, "Romance", null);
            lc.add(newCat1);
            Categories newCat2 = new Categories(3, "Mystery", null);
            lc.add(newCat2);
            Categories newCat3 = new Categories(4, "Fantasy & Science fiction", null);
            lc.add(newCat3);
            Categories newCat4 = new Categories(5, "Thrillers & Horror", null);
            lc.add(newCat4);
            Categories self_help = new Categories(6, "Self-help", null);
            lc.add(self_help);
            Categories short_stories = new Categories(7, "Short Stories", null);
            lc.add(short_stories);
            Categories cookbooks = new Categories(8, "Cookbooks", null);
            lc.add(cookbooks);
            Categories history = new Categories(9, "History", null);
            lc.add(history);
            Categories science = new Categories(10, "Science", null);
            lc.add(science);
            Categories adventure = new Categories(11, "Adventure", null);
            lc.add(adventure);
            Categories humor = new Categories(12, "Humor", null);
            lc.add(humor);
            Categories biography = new Categories(13, "Biography", null);
            lc.add(biography);
            Categories memoir = new Categories(14, "Memoir", null);
            lc.add(memoir);

            categoriesRepository.saveAll(lc);

            Set<Categories> newSet = new HashSet<>();
            newSet.add(newCat);
            newSet.add(newCat2);
            newSet.add(newCat3);
            newSet.add(newCat4);
            List<Categories> allCategories = categoriesRepository.findAll();

            Book ThinkAndGrowRich = new Book(24, "Think And Grow Rich", "Phuc", newSet);
            int n = 10;
            for (int i = 0; i < n; i++) {
                Set<Categories> randomCategories = new HashSet<>();
                Random random = new Random();
                int numCategories = random.nextInt(allCategories.size()) + 1; // Số lượng danh mục ngẫu nhiên từ 1 đến tất cả danh mục

                for (int j = 0; j < numCategories; j++) {
                    int randomIndex = random.nextInt(allCategories.size());
                    randomCategories.add(allCategories.get(randomIndex));
                }
                Book book = new Book(25, "book " + i,"thanhphuc",randomCategories);
                bookList.add(book);
            }
            bookList.add(ThinkAndGrowRich);
            bookRepository.saveAll(bookList);

            Cart cart = new Cart();
            List<CartItem> cartItems= new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                Book book = bookRepository.findById((long) i).orElse(null); // Lấy Book từ cơ sở dữ liệu
                if (book != null) {
                    CartItem cartItem = new CartItem();
                    cartItem.setQuantity(1);
                    cartItem.setBook(book);
                    cartItem.setCart(cart);
                    cartItems.add(cartItem);
                }
            }
            cart.setCartItems(new HashSet<>(cartItems));

            User Sor = new User(null, "Sor", "Q"
                    , "tthanhphuc752@gmail.com"
                    , applicationConfig.passwordEncoder().encode("123456")
                    , "USER", null, true, null, cart);
            userRepository.save(Sor);

            Order order = new Order();
            HashSet<Book> bookSet = new HashSet<>();
            bookSet.add(bookList.get(0));
            order.setBookList(bookSet);
            order.setDate(new Date());
            order.setUser(Sor);
            order.setPayment(25);
            order.setStatus(OrderStatus.PENDING);
            orderdetailRepository.save(order);

            Order order2 = new Order();
            HashSet<Book> bookSet2 = new HashSet<>();
            bookSet2.add(bookList.get(1));
            order2.setBookList(bookSet);
            order2.setDate(new Date());
            order2.setUser(Sor);
            order2.setPayment(50);
            order2.setStatus(OrderStatus.PROCESSING);
            orderdetailRepository.save(order2);
        };
    }
}
