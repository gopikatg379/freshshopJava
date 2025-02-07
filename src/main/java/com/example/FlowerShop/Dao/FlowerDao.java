package com.example.FlowerShop.Dao;

import com.example.FlowerShop.models.Flower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowerDao extends JpaRepository<Flower,Integer> {
}
