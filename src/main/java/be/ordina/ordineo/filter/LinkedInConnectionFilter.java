package be.ordina.ordineo.filter;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.linkedin.api.LinkedIn;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by gide on 17/03/16.
 */
public class LinkedInConnectionFilter implements Filter {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //do nothing
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Connection<LinkedIn> primaryConnection = connectionRepository.findPrimaryConnection(LinkedIn.class);

        HttpServletResponse response = (HttpServletResponse)servletResponse;

        if (primaryConnection == null) {
            authorizationNeeded(response);
            return;
        }

        if ( primaryConnection.hasExpired()) {
            primaryConnection.refresh();
        }

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (NotAuthorizedException e) {
            connectionRepository.removeConnection( primaryConnection.getKey() );

            authorizationNeeded(response);
            return;
        }
    }

    public void authorizationNeeded(HttpServletResponse response) throws IOException {
        response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
        response.setContentType("application/json");

        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode node = factory.objectNode();
        node.put("message", "No connection - authorization with linkedIn needed");

        response.getWriter().print( node.toString() );
    }

    @Override
    public void destroy() {
        //do nothing
    }

}
