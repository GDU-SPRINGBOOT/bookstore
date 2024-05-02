package com.example.bookstorebackend.Presentation.Controller.UserController;


import com.example.bookstorebackend.Domain.Model.User.User;
import com.example.bookstorebackend.Domain.Respones.ResponseObject;
import com.example.bookstorebackend.Domain.Respones.ResponseUtils;
import com.example.bookstorebackend.Domain.UserService.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/admin/user")
public class AdminUserController {

    private final UserServices userServices;


    @GetMapping("/findbyid")
    public ResponseEntity<ResponseObject> findUserByID(@RequestParam Long id) throws Exception {
        try{
            User user =  userServices.findUserByID(id);
            return ResponseUtils.buildSuccessResponse(user,"Successfully retrieved data");
        }catch (Exception e)
        {
            return ResponseUtils.buildErrorResponse(HttpStatus.BAD_REQUEST, "Failed to find user: " + e.getMessage());
        }

    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseObject> getAllUser() {
        try{
            List<User> userList =  userServices.getAllUser();
            return ResponseUtils.buildSuccessResponse(userList,"Successfully retrieved data");
        }catch (Exception e)
        {
            return ResponseUtils.buildErrorResponse(HttpStatus.BAD_REQUEST, "Failed to get list of users: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseObject> deleteUser(@RequestParam long id) {
        try{
            userServices.deleteUser(id);
            return ResponseUtils.buildSuccessResponse(null,"Successfully deleted user");
        }catch (Exception e)
        {
            return ResponseUtils.buildErrorResponse(HttpStatus.BAD_REQUEST, "Failed to delete user: " + e.getMessage());
        }
    }

    @GetMapping("/findbyemail")
    public ResponseEntity<ResponseObject> findByEmail(@RequestParam String s) {
        try{
            Optional<User> user =  userServices.findByEmail(s);
            return ResponseUtils.buildSuccessResponse(user,"Successfully retrieved data");
        }catch (Exception e)
        {
            return ResponseUtils.buildErrorResponse(HttpStatus.BAD_REQUEST, "Failed to find user: " + e.getMessage());
        }
    }

}
