package com.example.bookstorebackend.Domain.UserService;

import com.example.bookstorebackend.Domain.CartService.CartService;
import com.example.bookstorebackend.Domain.Model.Cart.Cart;
import com.example.bookstorebackend.Domain.Model.User.User;
import com.example.bookstorebackend.Domain.Model.User.UserAuthDetails;
import com.example.bookstorebackend.Domain.Model.token.VerificationToken;
import com.example.bookstorebackend.Domain.Request.RegistrationRequest;
import com.example.bookstorebackend.Domain.Security.JWTAuth.JwtService;
import com.example.bookstorebackend.Persistence.DAO.UserRepository;
import com.example.bookstorebackend.Persistence.DAO.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServicesImp implements UserServices {

    private final UserRepository userrepos;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;
    private final JwtService jwtService;

    private final Logger logger = LoggerFactory.getLogger(UserServicesImp.class);

    public List<User> getAllUser() {
        return userrepos.findAll();
    }

    @Override
    public User registerUser(RegistrationRequest request) {
        Optional<User> existingUser = this.findByEmail(request.email());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User with email " + request.email() + " already exists");
        }

        User newUser = new User();
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole("USER"); //

        Cart cart = new Cart();
        newUser.setCart(cart);

        var user1 = new UserAuthDetails(newUser);
        var jwtToken = jwtService.generateToken(user1);
        logger.info("JWT:" + jwtToken);

        return userrepos.save(newUser);
    }


    @Override
    public Optional<User> findByEmail(String email) {
        return userrepos.findByEmail(email);
    }

    public long countUser() {
        if (userrepos.count() <= 0) {
            return 0;
        }
        return userrepos.count();
    }

    public User findUserByID(@RequestParam long id) throws Exception {
        Optional<User> userOptional = userrepos.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new Exception("User with id: " + id + "does not exist");
        }
    }

    public boolean isEmailExists(String email) {
        return userrepos.existsByEmail(email);
    }

    @Override
    public void deleteUser(long userID) {
        boolean isExist = userrepos.existsById(userID);
        if (isExist) {
            userrepos.deleteById(userID);
        } else throw new IllegalStateException("User with ID: " + userID + " does not exist");
    }

    @Override
    public void saveUserVerificationToken(User theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        tokenRepository.save(verificationToken);
    }

}