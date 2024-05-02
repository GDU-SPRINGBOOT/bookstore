package com.example.bookstorebackend.Domain.Security.JWTAuth;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // Ghi lại thông báo lỗi
        System.out.println("Unauthorized error: " + authException.getMessage());

        // Trả về mã lỗi 401 và thông báo lỗi JSON
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized request");
    }
}
