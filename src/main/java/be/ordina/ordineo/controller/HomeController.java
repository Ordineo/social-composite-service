package be.ordina.ordineo.controller;

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
@RequestMapping("/api")
public class HomeController {

    private final LinkedIn linkedIn;

    @Inject
    public HomeController(LinkedIn linkedIn) {
        this.linkedIn = linkedIn;
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String home(Model model) {
        //connect/linkedin
        model.addAttribute("firstName", linkedIn.profileOperations().getUserProfile().getFirstName());
        return "home";
    }

}
