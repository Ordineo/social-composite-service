package be.ordina.ordineo.client.impl;

import be.ordina.ordineo.client.ImageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by gide on 29/03/16.
 */
@Component
public class ImageClientImpl implements ImageClient {

    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity synchronizeProfilePicture(String username, String profilePictureUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("url", profilePictureUrl);

        return restTemplate.postForObject("https://image-ordineo/api/images/url/{username}", new HttpEntity(profilePictureUrl), ResponseEntity.class, username);
    }

}
