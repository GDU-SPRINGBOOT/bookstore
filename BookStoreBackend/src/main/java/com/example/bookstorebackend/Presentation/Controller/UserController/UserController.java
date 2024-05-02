package com.example.bookstorebackend.Presentation.Controller.UserController;

import com.example.bookstorebackend.Domain.Request.RegistrationRequest;
import com.example.bookstorebackend.Domain.Model.User.User;
import com.example.bookstorebackend.Domain.UserService.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServices userServices;

    public User findUserByID(Long id) throws Exception {
        return userServices.findUserByID(id);
    }

    public List<User> getAllUser() {
        return userServices.getAllUser();
    }

    public void deleteUser(long userID) {
        userServices.deleteUser(userID);
    }

    public Optional<User> findByEmail(String s) {
        return userServices.findByEmail(s);
    }

    public User registerUser(RegistrationRequest request) {
        return userServices.registerUser(request);
    }

}
