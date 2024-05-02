package com.example.bookstorebackend.Presentation.Controller.OrderController;

import com.example.bookstorebackend.Domain.Model.Order.Order;
import com.example.bookstorebackend.Domain.Model.User.User;
import com.example.bookstorebackend.Domain.OrderService.AdminOrderService;
import com.example.bookstorebackend.Domain.Respones.ResponseObject;
import com.example.bookstorebackend.Domain.Respones.ResponseUtils;
import com.example.bookstorebackend.Domain.Security.JWTAuth.JwtService;
import com.example.bookstorebackend.Presentation.Controller.UserController.UserController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("api/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {
    private final AdminOrderService adminOrderService;
    private final UserController userController;
    private final JwtService jwtService;

    @GetMapping("/getAll")
    public ResponseEntity<ResponseObject> getAllOrders(HttpServletRequest request, HttpSession session) {
        if (!isAuthenticated(request)) {
            return ResponseUtils.buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication failed");

        }
        try {
            List<Order> orders = adminOrderService.getAllOrder(request, session);
            List<Object> reponse = new ArrayList<>();

            for (Order order : orders) {
                Map<String, Object> orderData = responseOrder(order);
                reponse.add(orderData);
            }
            return ResponseUtils.buildSuccessResponse(reponse, "Get all order successfully");
        } catch (Exception e) {
            return ResponseUtils.buildErrorResponse(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }

    @GetMapping("{orderId}")
    public ResponseEntity<ResponseObject> getOrderById(@PathVariable Long orderId, HttpServletRequest request, HttpSession session) {
        Optional<Order> order = adminOrderService.findOrderById(orderId, request, session);
        return order.map(
                value -> ResponseUtils.buildSuccessResponse(responseOrder(value), "Get order successfully"))
                .orElseGet(
                        () -> ResponseUtils.buildErrorResponse(HttpStatus.NOT_FOUND, "Order not found")
                );
    }

    @GetMapping("/status")
    public ResponseEntity<ResponseObject> getOrderByStatus(@RequestParam String s, HttpServletRequest request, HttpSession session) {
        if (!isAuthenticated(request)) {
            return ResponseUtils.buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication failed");
        }
        try {
            List<Order> orders = adminOrderService.findOrderByStatus(s, request, session);
            List<Object> reponse = new ArrayList<>();
            for (Order order : orders) {
                Map<String, Object> orderData = responseOrder(order);
                reponse.add(orderData);
            }
            return ResponseUtils.buildSuccessResponse(reponse, "Get order by status successfully");
        } catch (Exception e) {
            return ResponseUtils.buildErrorResponse(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<ResponseObject> getOrderByUser(@RequestParam Long id, HttpServletRequest request, HttpSession session) {
        if (!isAuthenticated(request)) {
            return ResponseUtils.buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication failed");
        }
        try {
            List<Order> orders = adminOrderService.findOrderByUser(id, request, session);
            List<Object> reponse = new ArrayList<>();
            for (Order order : orders) {
                Map<String, Object> orderData = responseOrder(order);
                reponse.add(orderData);
            }
            return ResponseUtils.buildSuccessResponse(reponse, "Get order by user successfully");
        } catch (Exception e) {
            return ResponseUtils.buildErrorResponse(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }
    @PostMapping("/update/{orderId}")
    public ResponseEntity<ResponseObject> updateOrderStatus(@PathVariable Long orderId, @RequestParam String s, HttpServletRequest request, HttpSession session) {
        if (!isAuthenticated(request)) {
            return ResponseUtils.buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication failed");
        }
        try {
            adminOrderService.updateOrderStatus(orderId, s, request, session);
            return ResponseUtils.buildSuccessResponse(null, "Update order status successfully");
        } catch (Exception e) {
            return ResponseUtils.buildErrorResponse(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    private boolean isAuthenticated(HttpServletRequest request) {
        String token = getJwtFromRequest(request);
        if (token != null) {
            Optional<User> optionalUser = userController.findByEmail(jwtService.extractUsername(token));
            return optionalUser.isPresent();
        }
        return false;
    }
    private Map<String, Object> responseOrder(Order order){
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("id", order.getId());
        orderData.put("date", order.getDate());
        orderData.put("payment", order.getPayment());
        orderData.put("booklist", order.getBookList());
        orderData.put("status", order.getStatus());
        orderData.put("user_id", order.getUser().getUserID());
        return orderData;
    }
}
