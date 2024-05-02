package com.example.bookstorebackend.Domain.AuthService;


import com.example.bookstorebackend.Domain.Model.User.User;
import com.example.bookstorebackend.Domain.Model.User.UserAuthDetails;
import com.example.bookstorebackend.Domain.Model.token.VerificationToken;
import com.example.bookstorebackend.Domain.Request.AuthenticationRequest;
import com.example.bookstorebackend.Domain.Respones.AuthenticationResponse;
import com.example.bookstorebackend.Domain.Security.JWTAuth.JwtService;
import com.example.bookstorebackend.Persistence.DAO.UserRepository;
import com.example.bookstorebackend.Persistence.DAO.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class AuthenServiceImp implements AuthenService {
    private final UserRepository userrepos;
    private final VerificationTokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final VerificationTokenRepository verificationTokenRepository;
    private final Logger logger = LoggerFactory.getLogger(AuthenServiceImp.class);

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request, HttpSession session) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
            User user = userrepos.findByEmail(request.email())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng cho email: " + request.email()));

            var user1 = new UserAuthDetails(user);
            logger.info("JWT: " + jwtService.generateToken(user1));

            String role = user.getRole();
            String name = user.getLastName();
            String username = user.getEmail();
            String jwtToken = jwtService.generateToken(user1);
            logger.info("Role: " + user1.getAuthorities());
            System.out.println("ROLE: " + user1.getAuthorities());
            session.setAttribute("USER",user);
            return AuthenticationResponse.builder().username(username).role(role).name(name).token(jwtToken).build();
        } catch (
                BadCredentialsException ex) {
            return AuthenticationResponse.builder().message("Wrong username or password").build();
        } catch (Exception ex) {
            return AuthenticationResponse.builder().message("Account is disable").build();
        }
    }

    public String validateToken(String theToken) {
        VerificationToken token = tokenRepository.findByToken(theToken);
        if (token == null) {
            return "Invalid verification token";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            tokenRepository.delete(token);
            return "Token already expired";
        }
        user.setEnabled(true);
        userrepos.save(user);
        return "Valid";
    }

    @Override
    public VerificationToken getToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }
}