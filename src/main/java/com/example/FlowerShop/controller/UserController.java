package com.example.FlowerShop.controller;

import com.example.FlowerShop.Services.UserService;
import com.example.FlowerShop.models.User;
import com.example.FlowerShop.security.JwtUtil;
import com.example.FlowerShop.security.TokenBlacklist;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenBlacklist tokenBlacklist;

    private final JwtUtil jwtUtil;

    public UserController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

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
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(HttpServletRequest request) {
        // Extract token from Authorization header
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Invalid Token");
        }

        String token = authorizationHeader.substring(7);
        String email = jwtUtil.extractEmail(token); // Extract username from token

        // Fetch user details from DB
        Optional<User> user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        return ResponseEntity.ok(user);
    }
}
