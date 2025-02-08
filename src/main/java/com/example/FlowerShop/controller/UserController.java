package com.example.FlowerShop.controller;

import com.example.FlowerShop.Services.UserService;
import com.example.FlowerShop.models.User;
import com.example.FlowerShop.security.TokenBlacklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenBlacklist tokenBlacklist;
    @PostMapping("register")
    public ResponseEntity<String>registerUser(
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("image") MultipartFile image) throws IOException {
        return userService.registerUser(name, password, email, image);
    }

    @PostMapping("login")
    public ResponseEntity<String> loginUser(@RequestParam("email") String email,@RequestParam("password") String password){
        return userService.loginUser(email,password);
    }

    @GetMapping("all")
    public ResponseEntity<List<User>> allUser(){
        return userService.allUser();
    }

    @PostMapping("logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        tokenBlacklist.addToBlacklist(jwtToken);

        return ResponseEntity.ok("Logged out successfully");

    }
}
