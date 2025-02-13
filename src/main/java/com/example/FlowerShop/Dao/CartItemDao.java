package com.example.FlowerShop.Dao;

import com.example.FlowerShop.models.Cart;
import com.example.FlowerShop.models.CartItems;
import com.example.FlowerShop.models.Flower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemDao extends JpaRepository<CartItems,Integer> {
    Optional<CartItems> findByCartAndFlower(Cart cart, Flower flower);
}
