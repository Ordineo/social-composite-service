package be.ordina.ordineo.controller;

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
        if (connectionRepository.findPrimaryConnection(LinkedIn.class) == null) {
            return "redirect:/connect/linkedin";
        }

        model.addAttribute("firstName", linkedIn.profileOperations().getUserProfile().getFirstName());
        return "home";
    }

}
