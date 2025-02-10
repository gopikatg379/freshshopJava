package com.example.FlowerShop.Dao;

import com.example.FlowerShop.models.Cart;
import com.example.FlowerShop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartDao extends JpaRepository<Cart,Integer> {
    Optional<Cart> findByUser(User user);
}
