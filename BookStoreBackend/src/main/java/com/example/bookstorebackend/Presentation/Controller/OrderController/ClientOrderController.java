package com.example.bookstorebackend.Presentation.Controller.OrderController;

import com.example.bookstorebackend.Domain.Model.Order.Order;
import com.example.bookstorebackend.Domain.Model.User.User;
import com.example.bookstorebackend.Domain.OrderService.ClientOrderService;
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
@RequestMapping("api/client/order")
@RequiredArgsConstructor
public class ClientOrderController {

    private final ClientOrderService clientOrderService;
    private final UserController userController;
    private final JwtService jwtService;

    @GetMapping("/getAll")
    public ResponseEntity<ResponseObject> getAllOrders(HttpServletRequest request, HttpSession session) {
        if (!isAuthenticated(request)) {
            return ResponseUtils.buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication failed");

        }
        try {
            List<Order> orders = clientOrderService.getAllOrder(request, session);
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
        Optional<Order> order = clientOrderService.findOrderById(orderId, request, session);
        return order.map(
                value -> ResponseUtils.buildSuccessResponse(responseOrder(value), "Get order successfully"))
                .orElseGet(
                        () -> ResponseUtils.buildErrorResponse(HttpStatus.NOT_FOUND, "Order not found")
                );
    }
    @GetMapping()
    public ResponseEntity<ResponseObject> getOrderByStatus(@RequestParam String s, HttpServletRequest request, HttpSession session) {
        if (!isAuthenticated(request)) {
            return ResponseUtils.buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication failed");
        }
        try {
            List<Order> orders = clientOrderService.findOrderByStatus(s, request, session);
            List<Object> response = new ArrayList<>();
            for (Order order : orders) {
                Map<String, Object> orderData = responseOrder(order);
                response.add(orderData);
            }
            return ResponseUtils.buildSuccessResponse(response, "Get order by status successfully");
        } catch (Exception e) {
            return ResponseUtils.buildErrorResponse(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }

    @PostMapping("{orderId}")
    public ResponseEntity<ResponseObject> cancelOrder(@PathVariable Long orderId, HttpServletRequest request, HttpSession session) {
        if (!isAuthenticated(request)) {
            return ResponseUtils.buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication failed");
        }
        try {
            clientOrderService.cancelOrder(orderId, request, session);
            return ResponseUtils.buildSuccessResponse(null, "Cancel order successfully");
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
        return orderData;
    }
}
