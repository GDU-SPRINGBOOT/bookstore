package com.example.bookstorebackend.Presentation.Controller.Authentication;

import com.example.bookstorebackend.Domain.AuthService.AuthenService;
import com.example.bookstorebackend.Domain.Request.AuthenticationRequest;
import com.example.bookstorebackend.Domain.Model.User.User;
import com.example.bookstorebackend.Domain.Model.token.VerificationToken;
import com.example.bookstorebackend.Domain.Request.RegistrationRequest;
import com.example.bookstorebackend.Domain.Respones.*;
import com.example.bookstorebackend.Domain.event.RegistrationCompleteEvent;
import com.example.bookstorebackend.Presentation.Controller.UserController.UserController;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final UserController userController;
    private final AuthenService authenService;
    private final ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request) {
        User user = userController.registerUser(registrationRequest);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        RegistrationResponse response = RegistrationResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail()).build();
        return ResponseUtils.buildCreatedResponse(response,"Register successfully");
    }

    @GetMapping("/verifyEmail")
    public ResponseEntity<Object> verifyEmail(@RequestParam("token") String token) {
        VerificationToken theToken = authenService.getToken(token);
        String message = "";

        if (theToken == null) {
            message = "Invalid token.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getResponse(false, message));
        }

        if (theToken.getUser().isEnabled()) {
            message = "This account has already been verified. Please login.";
            return ResponseEntity.ok(getResponse(true, message));
        }

        String verificationResult = authenService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")) {
            message = "Email verified successfully. Now you can login.";
            return ResponseEntity.ok(getResponse(true, message));
        }

        message = "Invalid verification token.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getResponse(false, message));
    }

    private Object getResponse(boolean success, String message) {
        return Map.of("success", success, "message", message);
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request,HttpSession session) {
        AuthenticationResponse response = authenService.authenticate(request,session);
        if (response.message() != null) {
            return ResponseEntity.badRequest().body(response);
        } else {
            return ResponseEntity.ok(response);
        }
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":"
                + request.getServerPort() + request.getContextPath();

    }


}