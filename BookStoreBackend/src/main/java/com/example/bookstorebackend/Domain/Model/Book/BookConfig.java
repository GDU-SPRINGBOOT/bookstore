package com.example.bookstorebackend.Domain.Model.Book;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class BookConfig {


    @Bean(name = "ProductBean")
    CommandLineRunner commandLineRunner() {
        return args -> {

        };
    }
}
