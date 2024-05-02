package com.example.bookstorebackend.Persistence.DAO;

import com.example.bookstorebackend.Domain.Model.Cart.Cart;
import com.example.bookstorebackend.Domain.Model.Cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findById(Long id);
}
