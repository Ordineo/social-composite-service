package be.ordina.ordineo.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;

/**
 * Created by gide on 17/03/16.
 */
@Controller
@RequestMapping("/api/linkedIn")
public class LinkedInRestController {

    private final LinkedIn linkedIn;

    @Inject
    public LinkedInRestController(LinkedIn linkedIn) {
        this.linkedIn = linkedIn;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity syncProfile(@RequestParam String username, Model model) {
        //passing username as a parameter so SocialConfig.RequestUsernameSource can fetch it

        //TODO Hans: pass username & linkedIn object to your service; add service with @Autowired :)
        return ResponseEntity.ok(null);
    }

}
