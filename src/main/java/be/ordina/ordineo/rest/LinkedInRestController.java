package be.ordina.ordineo.rest;

import be.ordina.ordineo.resource.EmployeeResource;
import be.ordina.ordineo.service.LinkedInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;

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



    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public ResponseEntity<EmployeeResource> syncProfile(@PathVariable String username) {
        //passing username as a parameter so SocialConfig.RequestUsernameSource can fetch it
        EmployeeResource employee =   service.ApplyLinkedInDataToEmployee(username,linkedIn);
        return ResponseEntity.ok(employee);
    }

}
