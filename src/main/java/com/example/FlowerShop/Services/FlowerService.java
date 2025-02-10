package com.example.FlowerShop.Services;

import com.example.FlowerShop.Dao.FlowerDao;
import com.example.FlowerShop.models.Flower;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class FlowerService {
    @Autowired
    FlowerDao flowerDao;
    private static final String UPLOAD_DIR = "uploads/";

    public ResponseEntity<String> addFlower(String flowerName, String color, Double price, String description, MultipartFile image) throws IOException {
            if (image.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty.");
            }
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.createDirectories(path.getParent()); // Ensure directory exists
            Files.write(path, image.getBytes());
            Flower flower = new Flower();
            flower.setFlower_name(flowerName);
            flower.setColor(color);
            flower.setPrice(price);
            flower.setDescription(description);
            flower.setImage(fileName);
            flowerDao.save(flower);
            return new ResponseEntity<>("success",HttpStatus.OK);
    }

    public ResponseEntity<List<Flower>> getFlower() {
        return new ResponseEntity<>(flowerDao.findAll(),HttpStatus.OK);
    }

    public ResponseEntity<Optional<Flower>> getOneFlower(Integer flower_id) {
        return new ResponseEntity<>(flowerDao.findById(flower_id),HttpStatus.OK);
    }

    public ResponseEntity<List<Flower>> searchFlower(String flower) {
        return new ResponseEntity<>(flowerDao.findByFlowerNameContainingIgnoreCase(flower),HttpStatus.OK);
    }
}
