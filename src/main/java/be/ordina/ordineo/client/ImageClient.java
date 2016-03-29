package be.ordina.ordineo.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;

/**
 * Created by gide on 29/03/16.
 */
public interface ImageClient {

    ResponseEntity synchronizeProfilePicture(String username, String profilePictureUrl);

}
