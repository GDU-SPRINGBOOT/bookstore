package com.example.bookstorebackend.Domain.OrderService;

import com.example.bookstorebackend.Domain.Model.Order.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

public interface AdminOrderService {
    List<Order> getAllOrder(HttpServletRequest request, HttpSession session);
    Optional<Order> findOrderById(Long id, HttpServletRequest request, HttpSession session);
    List<Order> findOrderByStatus(String status, HttpServletRequest request, HttpSession session);
    List<Order> findOrderByUser(Long userId, HttpServletRequest request, HttpSession session);
    void updateOrderStatus(Long id, String status, HttpServletRequest request, HttpSession session);
}
