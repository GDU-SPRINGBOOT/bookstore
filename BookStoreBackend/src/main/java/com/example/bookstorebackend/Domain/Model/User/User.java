package com.example.bookstorebackend.Domain.Model.User;

import com.example.bookstorebackend.Domain.Model.Cart.Cart;
import com.example.bookstorebackend.Domain.Model.Order.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name = "userinfo")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "userID")
    private Long userID;

    private String firstName;
    private String lastName;
    @Column(name = "email",
            unique = true)
    @NaturalId(mutable = true)
    private String email;
    private String password;
    private String role;
    private String token;
    private boolean isEnabled = false;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name= "cartID", referencedColumnName ="cartID")
    private Cart cart;

    @Override
    public String toString() {
        return "User{" +
                "id=" + userID +
                ", name='" + firstName + '\'' +
                " email='" + email + '\'' +
                '}';
    }
}
