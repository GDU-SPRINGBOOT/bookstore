package com.example.bookstorebackend.Domain.Model.Order;

import com.example.bookstorebackend.Domain.Model.Book.Book;
import com.example.bookstorebackend.Domain.Model.Cart.CartItem;
import com.example.bookstorebackend.Domain.Model.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "orderdetail")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderID")
    private long id;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "order_book",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "bookID")
    )
    private Set<Book> bookList = new HashSet<>();

    private double payment;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order(Set<Book> book, Date date, User user) {
        this.bookList = book;
        this.date = date;
        this.user = user;
    }

    public Order() {
    }

    public void setBookList(Set<Book> bookList) {
        this.bookList = bookList;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", date=" + date +
                '}';
    }
}
