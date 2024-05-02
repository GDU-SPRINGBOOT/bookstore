package com.example.bookstorebackend.Persistence.DAO;

import com.example.bookstorebackend.Domain.Model.Order.Order;
import com.example.bookstorebackend.Domain.Model.Order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderdetailRepository extends JpaRepository<Order, Long> {
    //Admin
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Optional<Order> admin_findById(@Param("id") Long id);
    @Query("SELECT o FROM Order o WHERE o.status = :status")
    List<Order> admin_findByStatus(@Param("status") OrderStatus status);

    //Client
    @Query("SELECT o FROM Order o WHERE o.user.userID = :userId")
    List<Order> findByUserId(@Param("userId") Long userId);

    @Query("SELECT o FROM Order o WHERE o.user.userID = :userId AND o.id = :id")
    Optional<Order> user_findById(Long userId, Long id);

    @Query("SELECT o FROM Order o WHERE o.user.userID = :userId AND o.status = :status")
    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);
}
