package com.example.bookstorebackend.Domain.Model.Cart;


import com.example.bookstorebackend.Domain.Model.Book.Book;
import com.example.bookstorebackend.Domain.Model.Order.Order;
import com.example.bookstorebackend.Domain.Model.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cartitem")
public class CartItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartItemID")
    private Long id;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "cartID",nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "bookID",nullable = false)
    private Book book;
}
