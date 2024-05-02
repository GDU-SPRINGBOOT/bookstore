package com.example.bookstorebackend.Domain.Model.Order;

import com.example.bookstorebackend.Persistence.DAO.OrderdetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class OrderConfig {

    private final List<Order> orderList = new ArrayList<>();

    @Bean(name = "OrderBean")
    CommandLineRunner commandLineRunner(OrderdetailRepository orderdetailRepository) {
        return args -> {


        };
    }
}
