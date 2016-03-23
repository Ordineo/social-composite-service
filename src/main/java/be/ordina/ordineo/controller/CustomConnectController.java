package be.ordina.ordineo.controller;

import be.ordina.ordineo.filter.LinkedInConnectionFilter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by gide on 23/03/16.
 */
@Controller
public class CustomConnectController extends ConnectController {

    @Inject
    public CustomConnectController(
            ConnectionFactoryLocator connectionFactoryLocator,
            ConnectionRepository connectionRepository) {
        super(connectionFactoryLocator, connectionRepository);
    }

    @Override
    protected String connectedView(String providerId){
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        String originalUrl = (String)request.getSession().getAttribute(LinkedInConnectionFilter.ORGINAL_URL);

        if (StringUtils.hasText( originalUrl )) {
            return "redirect:" + originalUrl;
        }

        return super.connectedView(providerId);
    }

}
