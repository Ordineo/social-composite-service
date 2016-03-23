package be.ordina.ordineo.filter;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.web.util.NestedServletException;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by gide on 17/03/16.
 */
public class LinkedInAuthorizationFilter implements Filter {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //do nothing
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Connection<LinkedIn> primaryConnection = findPrimaryLinkedInConnection();

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
        /*} catch (NotAuthorizedException e) {
            connectionRepository.removeConnection( primaryConnection.getKey() );

            authorizationNeeded(response);
            return;*/
        } catch (NestedServletException e) {
            if (e.getCause() instanceof  NotAuthorizedException) {
                connectionRepository.removeConnection( primaryConnection.getKey() );

                authorizationNeeded(response);
                return;
            } else {
                throw e;
            }
        }
    }

    private Connection<LinkedIn> findPrimaryLinkedInConnection() {
        try {
            return connectionRepository.findPrimaryConnection(LinkedIn.class);
        } catch (IndexOutOfBoundsException e) {
            /*
            This catch is needed due to a bug in the connection repository
            When removing the only existing connection the repository doesn't check on the size; it just gets the first element...
             */
            return null;
        }
    }

    protected void authorizationNeeded(HttpServletResponse response) throws IOException {
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
