package com.example.bookstorebackend.Domain.Model.Book;

import com.example.bookstorebackend.Domain.Model.Cart.CartItem;
import com.example.bookstorebackend.Domain.Model.Order.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookID")
    private long id;
    private double price;
    @Column(name = "book_image", columnDefinition = "VARBINARY(MAX)")
    private byte[] bookImage;
    private String name;
    private String author;
    private String description;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.MERGE, mappedBy = "bookList")
    private Set<Order> orders = new HashSet<>();

    //@JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "book_cate",
            joinColumns = @JoinColumn(name = "BookID"),
            inverseJoinColumns = @JoinColumn(name = "categoriesID")
    )
    private Set<Categories> categoriesSet = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private Set<CartItem> cartItems = new HashSet<>();

    public Book(double price, String name) {
        this.price = price;
        this.name = name;
    }

    public Book(double price, String name, String author, Set<Categories> categoriesSet) {
        this.price = price;
        this.name = name;
        this.author = author;
        this.categoriesSet = categoriesSet;
    }


    public Book() {
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", price=" + price +
                ", name='" + name + '\'' +
                '}';
    }
}
