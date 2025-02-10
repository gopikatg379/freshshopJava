package com.example.FlowerShop.Dao;

import com.example.FlowerShop.models.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemDao extends JpaRepository<CartItems,Integer> {
}
