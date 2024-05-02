package com.example.bookstorebackend.Persistence.DAO;

import com.example.bookstorebackend.Domain.Model.Cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.user.userID = :userId")
    Cart findByUserId(@Param("userId") Long userId);




}