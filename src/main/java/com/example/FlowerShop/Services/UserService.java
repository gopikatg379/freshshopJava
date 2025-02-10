package com.example.FlowerShop.Services;

import com.example.FlowerShop.Dao.UserDao;
import com.example.FlowerShop.models.User;
import com.example.FlowerShop.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService{
    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    private final String UPLOAD_DIR = "uploads/users/";

    public ResponseEntity<String> registerUser(String name, String password, String email, MultipartFile image){
        if(userDao.findByEmail(email).isPresent()){
            return new ResponseEntity<>("Email is already taken",HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        if (image != null && !image.isEmpty()) {
            try {
                File uploadFolder = new File(UPLOAD_DIR);
                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs(); // Create 'uploads/users/' if not exists
                }
                String filePath = UPLOAD_DIR + image.getOriginalFilename();
                image.transferTo(Paths.get(filePath));
                user.setImage("users/" + image.getOriginalFilename()); // Store subfolder path
            } catch (IOException e) {
                return new ResponseEntity<>("Failed to save image!",HttpStatus.BAD_REQUEST);
            }
        }
        userDao.save(user);
        return new ResponseEntity<>("user registered successfully", HttpStatus.OK) ;
    }

    public ResponseEntity<String> loginUser(String email, String password) {
        Optional<User> userOptional = userDao.findByEmail(email);
        System.out.println(userOptional);
        System.out.println("hi");
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return new ResponseEntity<>(jwtUtil.generateToken(email),HttpStatus.OK);  // ✅ Return JWT Token
            } else {
                return new ResponseEntity<>("Invalid password!",HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("User not found!",HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<List<User>> allUser() {
        return new ResponseEntity<>(userDao.findAll(),HttpStatus.OK);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword()) // Password stored in DB
                .roles("USER") // Assign default role
                .build();
    }

    public Optional<User> getUserByEmail(String email) {
        return userDao.findByEmail(email) ;
    }
}
