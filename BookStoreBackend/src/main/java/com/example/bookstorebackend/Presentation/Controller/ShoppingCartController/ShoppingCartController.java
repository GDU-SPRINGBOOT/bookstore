package com.example.bookstorebackend.Presentation.Controller.ShoppingCartController;

import com.example.bookstorebackend.Domain.BookService.ClientBookService;
import com.example.bookstorebackend.Domain.CartService.CartService;
import com.example.bookstorebackend.Domain.Model.Book.Book;
import com.example.bookstorebackend.Domain.Model.Cart.CartItem;
import com.example.bookstorebackend.Domain.Respones.ResponseObject;
import com.example.bookstorebackend.Domain.Respones.ResponseUtils;
import com.example.bookstorebackend.Presentation.Controller.BookController.ClientBookController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/client/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ClientBookService clientBookService;
    private final CartService cartService;
    @PostMapping("/add/{bookId}")
    public ResponseEntity<ResponseObject> addToCart(@PathVariable long bookId,HttpServletRequest request, HttpSession session)
    {
        Optional<Book> optionalBook = clientBookService.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            CartItem item = new CartItem();
            item.setBook(book);
            item.setQuantity(1);
            cartService.addToCart(item, request,session);
            return ResponseUtils.buildSuccessResponse(HttpStatus.OK,"Add to cart successfully");
        } else {
            return ResponseUtils.buildErrorResponse(HttpStatus.EXPECTATION_FAILED,"Add to cart false");
        }
    }

    @GetMapping("getAll")
    public ResponseEntity<ResponseObject> getAllCartItem(HttpSession session,HttpServletRequest request)
    {
        try{
            Collection<CartItem> cart = cartService.getAll(session,request);
            return ResponseUtils.buildSuccessResponse(cart,"Get all successfully");
        } catch(Exception e)
        {
            return ResponseUtils.buildErrorResponse(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }

    @GetMapping("{cartId}")
    public ResponseEntity<ResponseObject> getCartItem(@PathVariable Long cartId, HttpServletRequest request, HttpSession session)
    {
        try{
            CartItem cartItem = cartService.getCartItem(cartId,request,session);
            return ResponseUtils.buildSuccessResponse(cartItem,"Get all successfully");
        } catch(Exception e)
        {
            return ResponseUtils.buildErrorResponse(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }

    @PostMapping("delete/{bookId}")
    public ResponseEntity<ResponseObject> removeFromCart(@PathVariable Long bookId,HttpServletRequest request, HttpSession session)
    {
        try{
            cartService.removeFromCart(bookId,request,session);
            return ResponseUtils.buildSuccessResponse(null,"Get all successfully");
        } catch(Exception e)
        {
            return ResponseUtils.buildErrorResponse(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }

    @PostMapping("uppdate/{bookId}/{quantity}")
    public ResponseEntity<ResponseObject> uppdateCart(@PathVariable Long bookId, @PathVariable int quantity,HttpServletRequest request, HttpSession session)
    {
        try{
            cartService.updateCart(bookId,quantity,request,session);
            return ResponseUtils.buildSuccessResponse(null,"Get all successfully");
        } catch(Exception e)
        {
            return ResponseUtils.buildErrorResponse(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }

    @PostMapping("clearAll")
    public ResponseEntity<ResponseObject> clearAll(HttpSession session,HttpServletRequest request)
    {
        try{
            cartService.clearAll(session,request);
            return ResponseUtils.buildSuccessResponse(null,"Clear all successfully");
        } catch(Exception e)
        {
            return ResponseUtils.buildErrorResponse(HttpStatus.EXPECTATION_FAILED,e.getMessage());
        }
    }

    @PostMapping("checkout/{cartItemId}")
    public ResponseEntity<ResponseObject> checkout(@PathVariable Long cartItemId, HttpSession session, HttpServletRequest request)
    {
        try{
            cartService.checkout(cartItemId,session,request);
            return ResponseUtils.buildSuccessResponse(null,"Checkout successfully");
        } catch(Exception e)
        {
            return ResponseUtils.buildErrorResponse(HttpStatus.EXPECTATION_FAILED,e.getMessage());
        }
    }

}
