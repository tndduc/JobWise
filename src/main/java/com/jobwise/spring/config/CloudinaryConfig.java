package com.jobwise.spring.config;


import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/21/2023
 */
@Configuration
public class CloudinaryConfig {
    private final String CLOUD_NAME = "dpksmnl3p";
    private final String API_KEY = "148671664393439";
    private final String API_SECRET = "yd_ZostmmlyroO93Km2J7b8p2qA";
    @Bean
    public Cloudinary cloudinary(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name",CLOUD_NAME);
        config.put("api_key",API_KEY);
        config.put("api_secret",API_SECRET);
        return new Cloudinary(config);
    }
}