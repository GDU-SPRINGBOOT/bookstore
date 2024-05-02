package com.example.bookstorebackend.Domain.Model.Book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long categoriesID;
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "categoriesSet", cascade = CascadeType.ALL)
    private Set<Book> bookSet = new HashSet<>();


    public Categories(String name, Set<Book> bookSet) {
        this.name = name;
        this.bookSet = bookSet;
    }
}
