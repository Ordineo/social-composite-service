package be.ordina.ordineo.client.impl;

import be.ordina.ordineo.client.ImageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gide on 29/03/16.
 */
@Component
public class ImageClientImpl implements ImageClient {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity synchronizeProfilePicture(String username, String profilePictureUrl) {
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("username", username);

        Map<String, String> headers = new HashMap<>();
        headers.put("url", profilePictureUrl);

        return restTemplate.postForObject("https://image-ordineo/api/images/{username}", new HttpEntity(headers), ResponseEntity.class, urlVariables);
    }

}
