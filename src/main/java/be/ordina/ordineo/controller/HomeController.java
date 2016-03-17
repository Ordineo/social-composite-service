package be.ordina.ordineo.controller;

import org.springframework.social.NotAuthorizedException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

/**
 * Simple little @Controller that invokes Facebook and renders the result.
 * The injected {@link LinkedIn} reference is configured with the required authorization credentials for the current user behind the scenes.
 * @author Keith Donald
 */
@Controller
@RequestMapping("/")
public class HomeController {

    private final LinkedIn linkedIn;
    private final ConnectionRepository connectionRepository;

    @Inject
    public HomeController(LinkedIn linkedIn, ConnectionRepository connectionRepository) {
        this.linkedIn = linkedIn;
        this.connectionRepository = connectionRepository;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        Connection<LinkedIn> primaryConnection = connectionRepository.findPrimaryConnection(LinkedIn.class);

        if (primaryConnection == null) {
            return "redirect:/connect/linkedin";
        }

        if ( primaryConnection.hasExpired()) {
            primaryConnection.refresh();
        }

        try {
            model.addAttribute("firstName", linkedIn.profileOperations().getUserProfile().getFirstName());
            return "home";
        } catch (NotAuthorizedException e) {
            connectionRepository.removeConnection( primaryConnection.getKey() );
            return "redirect:/connect/linkedin";
        }
    }

}
