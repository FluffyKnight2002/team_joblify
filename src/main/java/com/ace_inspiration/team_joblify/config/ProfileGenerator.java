package com.ace_inspiration.team_joblify.config;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;

public class ProfileGenerator {

    public static byte[] generateAvatar(String username, ResourceLoader resourceLoader) {
        try {
            // Construct the API URL
            String apiUrl = "https://ui-avatars.com/api/?name=" + username + "&background=random&size=512&font-size=0.33&length=3&rounded=true";

            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(apiUrl, byte[].class);
        } catch (RestClientException e) {
            e.printStackTrace();
            try {
                Resource resource = resourceLoader.getResource("classpath:/static/assets/images/faces/5.jpg");
                try (InputStream inputStream = resource.getInputStream()) {
                    return StreamUtils.copyToByteArray(inputStream);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                // Handle the IOException properly, e.g., return a default image
                return new byte[0];
            }
        }
    }
}
