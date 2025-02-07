package com.example.FlowerShop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map "/uploads/**" to the physical folder containing images
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///C:/Users/hp/IdeaProjects/Projects/SpringBoot1/FlowerShop/uploads/");
    }
}

