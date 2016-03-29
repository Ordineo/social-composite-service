package be.ordina.ordineo.rest;

import be.ordina.ordineo.service.LinkedInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Created by gide on 17/03/16.
 */
@Controller
@RequestMapping("/api/linkedin")
public class LinkedInRestController {

    private final LinkedIn linkedIn;
    private final LinkedInService service;

    @Autowired
    public LinkedInRestController(LinkedIn linkedIn, LinkedInService service) {
        this.linkedIn = linkedIn;
        this.service = service;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity syncProfile(@RequestParam String username) throws IOException {
        //passing username as a parameter so SocialConfig.RequestUsernameSource can fetch it

        service.applyLinkedInDataToEmployee(username, linkedIn);
        service.applyUserProfilePicture(username, linkedIn);

        return ok().build();
    }

}
