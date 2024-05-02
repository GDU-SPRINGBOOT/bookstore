package com.example.bookstorebackend.Domain.CartService;

import com.example.bookstorebackend.Domain.Model.Cart.Cart;
import com.example.bookstorebackend.Domain.Model.Cart.CartItem;
import com.example.bookstorebackend.Domain.Model.User.User;
import com.example.bookstorebackend.Domain.OrderService.ClientOrderService;
import com.example.bookstorebackend.Persistence.DAO.CartItemRepository;
import com.example.bookstorebackend.Persistence.DAO.CartRepository;
import com.example.bookstorebackend.Persistence.DAO.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CartServiceImp implements CartService {

    private static final String SESSION_KEY_CART = "CART";
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ClientOrderService clientOrderService;

    @Override
    public Map<Long, CartItem> getCartMap(HttpSession session) {
        Map<Long, CartItem> cartMap = (Map<Long, CartItem>) session.getAttribute(SESSION_KEY_CART);
        if (cartMap == null) {
            cartMap = new HashMap<>();
            session.setAttribute(SESSION_KEY_CART, cartMap);
        }
        return cartMap;
    }

    private boolean isUserLoggedIn(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        return authHeader!=null;
    }

    private Long getUserId(HttpSession session) {
        User user = (User) session.getAttribute("USER");
        return user.getUserID();
    }

    private Map<Long, CartItem> getCartFromDatabase(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        Map<Long, CartItem> cartMap = new HashMap<>();
        if (cart != null) {
            for (CartItem cartItem : cart.getCartItems()) {
                cartMap.put(cartItem.getBook().getId(), cartItem);
            }
        }
        return cartMap;
    }

    private void storeCartInDatabase(Long userId, Map<Long, CartItem> cartMap) {
        Cart cart = cartRepository.findByUserId(userId);

        Set<CartItem> existingCartItems = new HashSet<>(cart.getCartItems());

        for (CartItem cartItem : cartMap.values()) {
            if (cartItem.getId() == null) {
                cartItem.setCart(cart);
                cart.getCartItems().add(cartItem);
            } else {
                CartItem existingCartItem = existingCartItems.stream()
                        .filter(item -> item.getId().equals(cartItem.getId()))
                        .findFirst()
                        .orElse(null);

                if (existingCartItem != null) {
                    existingCartItem.setQuantity(cartItem.getQuantity());
                } else {
                    cartItem.setCart(cart);
                    cart.getCartItems().add(cartItem);
                }
            }
        }

        existingCartItems.removeAll(cart.getCartItems());
        cart.getCartItems().removeAll(existingCartItems);
        cartRepository.save(cart);
    }

    @Override
    public void addToCart(CartItem item, HttpServletRequest request, HttpSession session) {
        if (isUserLoggedIn(request)) {
            Long userId = getUserId(session);
            Map<Long, CartItem> cartMap = getCartFromDatabase(userId);
            CartItem cartItem = cartMap.get(item.getBook().getId());
            if (cartItem == null) {
                cartMap.put(item.getBook().getId(), item);
            } else {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
            }
            storeCartInDatabase(userId, cartMap);
        } else {
            Map<Long, CartItem> cartMap = getCartMap(session);
            CartItem cartItem = cartMap.get(item.getBook().getId());
            if (cartItem == null) {
                cartMap.put(item.getBook().getId(), item);
            } else {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
            }
            session.setAttribute(SESSION_KEY_CART, cartMap);
        }
    }

    @Override
    public void removeFromCart(long bookId, HttpServletRequest request,HttpSession session) {
        if (isUserLoggedIn(request)) {
            Long userId = getUserId(session);
            Map<Long, CartItem> cartMap = getCartFromDatabase(userId);
            CartItem itemToRemove = cartMap.remove(bookId);
            if (itemToRemove != null) {
                itemToRemove.getCart().getCartItems().remove(itemToRemove);
                cartItemRepository.delete(itemToRemove);
            }
        } else {
            Map<Long, CartItem> cartMap = getCartMap(session);
            cartMap.remove(bookId);
            session.setAttribute(SESSION_KEY_CART, cartMap);
        }
    }

    @Override
    public void updateCart(Long bookId, int quantity,HttpServletRequest request, HttpSession session) {
        if (isUserLoggedIn(request)) {
            Long userId = getUserId(session);
            Cart cart = cartRepository.findByUserId(userId);
            if (cart != null) {
                CartItem cartItem = cart.getCartItems().stream()
                        .filter(item -> item.getBook().getId()==bookId)
                        .findFirst()
                        .orElse(null);
                if (cartItem != null) {
                    cartItem.setQuantity(quantity);
                    cartItemRepository.save(cartItem);
                }
            }
        } else {
            Map<Long, CartItem> cartMap = getCartMap(session);
            CartItem cartItem = cartMap.get(bookId);
            if (cartItem != null) {
                cartItem.setQuantity(quantity);
            }
            session.setAttribute(SESSION_KEY_CART, cartMap);
        }
    }

    @Override
    public void clearAll(HttpSession session,HttpServletRequest request) {
        if (isUserLoggedIn(request)) {
            Long userId = getUserId(session);
            Cart cart = cartRepository.findByUserId(userId);
            if (cart != null) {
                cart.getCartItems().clear();
                cartRepository.save(cart);
            }
        } else {
            Map<Long, CartItem> cartMap = getCartMap(session);
            cartMap.clear();
            session.setAttribute(SESSION_KEY_CART, cartMap);
        }
    }

    @Override
    public Collection<CartItem> getAll(HttpSession session,HttpServletRequest request) {
        if (isUserLoggedIn(request)) {
            Long userId = getUserId(session);
            Map<Long, CartItem> cartMap = getCartFromDatabase(userId);
            return cartMap.values();
        } else {
            Map<Long, CartItem> cartMap = getCartMap(session);
            return cartMap.values();
        }
    }

    @Override
    public CartItem getCartItem(Long cartItemId, HttpServletRequest request, HttpSession session) {
        if (isUserLoggedIn(request)) {
            Long userId = getUserId(session);
            Map<Long, CartItem> cartMap = getCartFromDatabase(userId);
            CartItem cartItem = new CartItem();

            for (Map.Entry<Long, CartItem> entry : cartMap.entrySet()) {
                if (entry.getValue().getId().equals(cartItemId)) {
                    cartItem = entry.getValue();
                    break;
                }
            }
            if (cartItem.getId() == null) {
                throw new RuntimeException("Cart item not found");
            }
            return cartItem;
        } else {
            Map<Long, CartItem> cartMap = getCartMap(session);
            return cartMap.get(cartItemId);
        }
    }

    @Override
    public void checkout(Long cartItemId, HttpSession session, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            throw new RuntimeException("User is not logged in");
        }
        Long userId = getUserId(session);
        CartItem cartItem = getCartItem(cartItemId, request, session);

        try {
            clientOrderService.addOrder(userId, cartItem, session);
            removeFromCart(cartItem.getBook().getId(), request, session);
        } catch (Exception e) {
            throw new RuntimeException("Failed to checkout");
        }
    }
}