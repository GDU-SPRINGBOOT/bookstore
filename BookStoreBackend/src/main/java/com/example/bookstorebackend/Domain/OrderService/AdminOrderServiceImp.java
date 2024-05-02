package com.example.bookstorebackend.Domain.OrderService;

import com.example.bookstorebackend.Domain.Model.Order.Order;
import com.example.bookstorebackend.Domain.Model.Order.OrderStatus;
import com.example.bookstorebackend.Domain.Model.User.User;
import com.example.bookstorebackend.Persistence.DAO.OrderdetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImp implements AdminOrderService{
    private final OrderdetailRepository orderdetailRepository;

    private boolean isAdminLoggedIn(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        return authHeader == null;
    }
    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("ADMIN");
        return !user.getRole().equals("ADMIN");
    }
    @Override
    public List<Order> getAllOrder(HttpServletRequest request, HttpSession session) {
        if (isAdminLoggedIn(request) && isAdmin(session)) {
            throw new RuntimeException("User is not logged in");
        }
        return orderdetailRepository.findAll();
    }

    @Override
    public Optional<Order> findOrderById(Long id, HttpServletRequest request, HttpSession session) {
        if (isAdminLoggedIn(request) && isAdmin(session)) {
            throw new RuntimeException("User is not logged in");
        }
        return orderdetailRepository.admin_findById(id);
    }
    @Override
    public List<Order> findOrderByStatus(String status, HttpServletRequest request, HttpSession session) {
        if (isAdminLoggedIn(request) && isAdmin(session)) {
            throw new RuntimeException("User is not logged in");
        }
        OrderStatus orderStatus = OrderStatus.fromString(status);
        return orderdetailRepository.admin_findByStatus(orderStatus);
    }
    @Override
    public List<Order> findOrderByUser(Long userId, HttpServletRequest request, HttpSession session) {
        if (isAdminLoggedIn(request) && isAdmin(session)) {
            throw new RuntimeException("User is not logged in");
        }
        return orderdetailRepository.findByUserId(userId);
    }
    @Override
    public void updateOrderStatus(Long id, String status, HttpServletRequest request, HttpSession session) {
        if (isAdminLoggedIn(request) && isAdmin(session)) {
            throw new RuntimeException("User is not logged in");
        }
        Order order = orderdetailRepository.admin_findById(id).get();
        OrderStatus orderStatus = OrderStatus.fromString(status);
        order.setStatus(orderStatus);
        orderdetailRepository.save(order);
    }
}
