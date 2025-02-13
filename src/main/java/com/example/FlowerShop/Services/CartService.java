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

    public ResponseEntity<String> addToCart(Integer userId, Integer flowerId) {
        Optional<User> userOptional = userDao.findById(userId);
        Optional<Flower> flowerOptional = flowerDao.findById(flowerId);

        User user = userOptional.get();
        Flower flower = flowerOptional.get();

        Cart cart = cartDao.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartDao.save(newCart);
        });

        Optional<CartItems> existingCartItem = cartItemDao.findByCartAndFlower(cart, flower);

        if (existingCartItem.isPresent()) {
            CartItems cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItemDao.save(cartItem);
        } else {
            CartItems newCartItem = new CartItems();
            newCartItem.setCart(cart);
            newCartItem.setFlower(flower);
            newCartItem.setQuantity(1);
            cartItemDao.save(newCartItem);
        }

        cartDao.save(cart);

        return new ResponseEntity<>("Item added to cart", HttpStatus.OK);
    }


    public ResponseEntity<List<Cart>> viewCart(Integer userId) {
        User user = userDao.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Cart> cart = cartDao.findByUser(user);
        return new ResponseEntity<>(cart.map(List::of).orElse(Collections.emptyList()),HttpStatus.OK);
    }

    public ResponseEntity<String> deleteCart(Integer cartId) {
        cartItemDao.deleteById(cartId);
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }
}
