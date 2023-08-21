package com.ace_inspiration.team_joblify.config;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ProfileGenerator {
    
    
    public static byte[] generateAvatar(String username) {
        try {
            // Construct the API URL
            String apiUrl = "https://ui-avatars.com/api/?name=" + username + "&background=random&size=512&font-size=0.4&length=3&rounded=true";

            // Make a request to the API and retrieve the avatar URL
            RestTemplate restTemplate = new RestTemplate();
            byte[] imageBytes = restTemplate.getForObject(apiUrl, byte[].class);
            return imageBytes;
        } catch (RestClientException e) {
            // Handle exception here, such as logging the error
            e.printStackTrace();
            return null; // or a default avatar byte array
        }
    }
}
