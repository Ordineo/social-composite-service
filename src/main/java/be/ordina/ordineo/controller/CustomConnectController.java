package be.ordina.ordineo.controller;

import be.ordina.ordineo.filter.LinkedInConnectionFilter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public RedirectView oauth2ErrorCallback(@PathVariable String providerId,
                                            @RequestParam("error") String error,
                                            @RequestParam(value="error_description", required=false) String errorDescription,
                                            @RequestParam(value="error_uri", required=false) String errorUri,
                                            NativeWebRequest request) {

        String originalUrl = originalUrl();
        if (StringUtils.hasText( originalUrl )) {
            originalUrl = addParameter(originalUrl, "status", HttpServletResponse.SC_UNAUTHORIZED+"");
            originalUrl = addParameter(originalUrl, "error_description", errorDescription);
            return new RedirectView(originalUrl);
        }

        return super.oauth2ErrorCallback(providerId, error, errorDescription, errorUri, request);
    }

    @Override
    protected String connectedView(String providerId) {
        String originalUrl = originalUrl();

        if (StringUtils.hasText( originalUrl )) {
            originalUrl = addParameter(originalUrl, "status", HttpServletResponse.SC_OK+"");
            return "redirect:" + originalUrl;
        }

        return super.connectedView(providerId);
    }

    private String originalUrl() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        return (String)request.getSession().getAttribute(LinkedInConnectionFilter.ORGINAL_URL);
    }

    private String addParameter(String url, String name, String value) {
        StringBuilder builder = new StringBuilder(url);

        builder.append( url.contains("?") ? "&" : "?" )
                .append(name)
                .append("=")
                .append(value);

        return builder.toString();
    }

}
