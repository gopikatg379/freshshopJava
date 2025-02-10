package com.example.FlowerShop.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "flower_table")
@Data
public class Flower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer flowerId;
    private String flowerName;
    private String color;
    private Double price;
    private String description;
    private String image;

    public Integer getFlower_id() {
        return flowerId;
    }

    public void setFlower_id(Integer flower_id) {
        this.flowerId = flower_id;
    }

    public String getFlower_name() {
        return flowerName;
    }

    public void setFlower_name(String flower_name) {
        this.flowerName = flower_name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
