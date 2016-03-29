package be.ordina.ordineo.service;

import be.ordina.ordineo.resource.EmployeeResource;
import org.springframework.http.ResponseEntity;
import org.springframework.social.linkedin.api.LinkedIn;

import java.io.IOException;

/**
 * Created by Hans on 17/03/16.
 */
public interface LinkedInService {

     void applyLinkedInDataToEmployee (String username , LinkedIn linkedIn);

     void applyUserProfilePicture(String username, LinkedIn linkedIn) throws IOException;

}
