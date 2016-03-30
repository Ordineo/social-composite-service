package be.ordina.ordineo.service;

import org.springframework.social.linkedin.api.LinkedIn;

import java.io.IOException;

/**
 * Created by Hans on 17/03/16.
 */
public interface LinkedInService {

    void synchronizeEmployee(String username, LinkedIn linkedIn);

}
