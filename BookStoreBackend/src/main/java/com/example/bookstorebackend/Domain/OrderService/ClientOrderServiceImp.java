package com.example.bookstorebackend.Domain.OrderService;

import com.example.bookstorebackend.Domain.Model.Book.Book;
import com.example.bookstorebackend.Domain.Model.Cart.CartItem;
import com.example.bookstorebackend.Domain.Model.Order.Order;
import com.example.bookstorebackend.Domain.Model.Order.OrderStatus;
import com.example.bookstorebackend.Domain.Model.User.User;
import com.example.bookstorebackend.Persistence.DAO.OrderdetailRepository;
import com.example.bookstorebackend.Persistence.DAO.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ClientOrderServiceImp implements ClientOrderService {
    private final OrderdetailRepository orderdetailRepository;
    private final UserRepository userRepository;

    private boolean isUserLoggedIn(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        return authHeader!=null;
    }
    private Long getUserId(HttpSession session) {
        User user = (User) session.getAttribute("USER");
        return user.getUserID();
    }
    @Override
    public void addOrder(Long userId, CartItem cartItem, HttpSession session) {
        User user = userRepository.findById(userId).get();
        Order newOrder = new Order();
        Set<Book> bookList = new HashSet<>();
        bookList.add(cartItem.getBook());
        newOrder.setDate(new Date());
        newOrder.setUser(user);
        newOrder.setBookList(bookList);
        newOrder.setStatus(OrderStatus.PENDING);

        double payment = cartItem.getBook().getPrice() * cartItem.getQuantity();
        newOrder.setPayment(payment);
        orderdetailRepository.save(newOrder);
    }

    @Override
    public List<Order> getAllOrder(HttpServletRequest request, HttpSession session) {
        if (!isUserLoggedIn(request)) {
            throw new RuntimeException("User is not logged in");
        }
        Long userId = getUserId(session);
        return orderdetailRepository.findByUserId(userId);
    }

    @Override
    public Optional<Order> findOrderById(Long id, HttpServletRequest request, HttpSession session) {
        if (!isUserLoggedIn(request)) {
            throw new RuntimeException("User is not logged in");
        }
        Long userId = getUserId(session);
        return orderdetailRepository.user_findById(userId, id);
    }

    @Override
    public void cancelOrder(Long id, HttpServletRequest request, HttpSession session) {
        if (!isUserLoggedIn(request)) {
            throw new RuntimeException("User is not logged in");
        }
        Long userId = getUserId(session);
        Optional<Order> order = orderdetailRepository.user_findById(userId, id);
        if (order.isEmpty()) {
            throw new RuntimeException("Order not found");
        }
        if (order.get().getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Order is already cancelled");
        }
        if (order.get().getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Order is not pending");
        }
        Order order1 = order.get();
        order1.setStatus(OrderStatus.CANCELLED);
        orderdetailRepository.save(order1);
    }
    @Override
    public List<Order> findOrderByStatus(String status, HttpServletRequest request, HttpSession session) {
        if (!isUserLoggedIn(request)) {
            throw new RuntimeException("User is not logged in");
        }
        Long userId = getUserId(session);
        OrderStatus orderStatus = OrderStatus.fromString(status);

        return orderdetailRepository.findByUserIdAndStatus(userId, orderStatus);
    }
}
