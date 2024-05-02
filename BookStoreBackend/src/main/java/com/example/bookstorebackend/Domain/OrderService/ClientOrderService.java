package com.example.bookstorebackend.Domain.OrderService;

import com.example.bookstorebackend.Domain.Model.Cart.CartItem;
import com.example.bookstorebackend.Domain.Model.Order.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
public interface ClientOrderService {
    void addOrder(Long userId, CartItem cartItem, HttpSession session);
    List<Order> getAllOrder(HttpServletRequest request, HttpSession session);
    Optional<Order> findOrderById(Long id, HttpServletRequest request, HttpSession session);
    void cancelOrder(Long id, HttpServletRequest request, HttpSession session);
    List<Order> findOrderByStatus(String status, HttpServletRequest request, HttpSession session);
}
