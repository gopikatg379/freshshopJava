package com.example.FlowerShop.controller;

import com.example.FlowerShop.Dao.UserDao;
import com.example.FlowerShop.Services.CartService;
import com.example.FlowerShop.models.Cart;
import com.example.FlowerShop.models.CartItems;
import com.example.FlowerShop.models.User;
import com.example.FlowerShop.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDao userDao;

    public Integer getUserIdFromToken(String token) {
        String email = jwtUtil.extractEmail(token); // Extract email from JWT
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getUserId(); // Return userId
    }

    @PostMapping("add")
    private ResponseEntity<String> addCart(@RequestHeader("Authorization") String token, @RequestParam Integer flowerId) {
        String jwt = token.substring(7);
        Integer userId = getUserIdFromToken(jwt);
        return cartService.addToCart(userId, flowerId);
    }

    @GetMapping("view")
    private ResponseEntity<List<Cart>> viewCart(@RequestHeader("Authorization") String token){
        String jwt = token.substring(7);
        Integer userId = getUserIdFromToken(jwt);
        return cartService.viewCart(userId);
    }

    @DeleteMapping("delete/{cartId}")
    private ResponseEntity<String>deleteCart(@PathVariable Integer cartId){
        return cartService.deleteCart(cartId);
    }

}
