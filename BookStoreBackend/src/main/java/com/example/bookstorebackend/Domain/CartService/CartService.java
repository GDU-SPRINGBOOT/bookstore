package com.example.bookstorebackend.Domain.CartService;

import com.example.bookstorebackend.Domain.Model.Cart.Cart;
import com.example.bookstorebackend.Domain.Model.Cart.CartItem;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Map;

@Service
public interface CartService {
    Map<Long, CartItem> getCartMap(HttpSession session);

    void addToCart(CartItem item, HttpServletRequest request, HttpSession session);

    void removeFromCart(long bookId, HttpServletRequest request,HttpSession session);

    void updateCart(Long bookId, int quantity, HttpServletRequest request,HttpSession session);

    void clearAll(HttpSession session,HttpServletRequest request);

    Collection<CartItem> getAll(HttpSession session,HttpServletRequest request);

    CartItem getCartItem(Long cartItemId, HttpServletRequest request, HttpSession session);

    void checkout(Long cartItemId, HttpSession session, HttpServletRequest request);

}
