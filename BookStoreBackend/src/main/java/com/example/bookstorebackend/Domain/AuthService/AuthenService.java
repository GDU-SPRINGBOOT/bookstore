package com.example.bookstorebackend.Domain.AuthService;

import com.example.bookstorebackend.Domain.Model.token.VerificationToken;
import com.example.bookstorebackend.Domain.Request.AuthenticationRequest;
import com.example.bookstorebackend.Domain.Respones.AuthenticationResponse;

import javax.servlet.http.HttpSession;

public interface AuthenService {
    AuthenticationResponse authenticate(AuthenticationRequest request, HttpSession session);

    String validateToken(String theToken);

    VerificationToken getToken(String token);
}