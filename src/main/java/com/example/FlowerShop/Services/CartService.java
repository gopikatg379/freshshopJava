package com.example.FlowerShop.Services;

import com.example.FlowerShop.Dao.CartDao;
import com.example.FlowerShop.Dao.CartItemDao;
import com.example.FlowerShop.Dao.FlowerDao;
import com.example.FlowerShop.Dao.UserDao;
import com.example.FlowerShop.models.Cart;
import com.example.FlowerShop.models.CartItems;
import com.example.FlowerShop.models.Flower;
import com.example.FlowerShop.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartDao cartDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CartItemDao cartItemDao;
    @Autowired
    private FlowerDao flowerDao;

    public ResponseEntity<String> addCart(Integer userId, Integer flowerId) {
        User user = userDao.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Flower flower = flowerDao.findById(flowerId)
                .orElseThrow(() -> new RuntimeException("Flower not found"));

        Cart cart = cartDao.findByUser(user).orElseGet(()->{
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartDao.save(newCart);
        });
        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>()); // ✅ Initialize if null
        }
        Optional<CartItems> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getFlower().equals(flower))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItems cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItemDao.save(cartItem);
        } else {
            CartItems cartItem = new CartItems();
            cartItem.setCart(cart);
            cartItem.setFlower(flower);
            cartItem.setQuantity(1);
            cartItemDao.save(cartItem);
        }
        return new ResponseEntity<>("added", HttpStatus.OK);
    }

    public ResponseEntity<List<Cart>> viewCart(Integer userId) {
        User user = userDao.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Cart> cart = cartDao.findByUser(user);
        return new ResponseEntity<>(cart.map(List::of).orElse(Collections.emptyList()),HttpStatus.OK);
    }
}
