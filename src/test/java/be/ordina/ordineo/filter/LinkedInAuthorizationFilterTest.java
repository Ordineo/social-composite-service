package be.ordina.ordineo.filter;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.web.util.NestedServletException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Hans on 23/03/16.
 */
public class LinkedInAuthorizationFilterTest {

    @Mock
    ConnectionRepository repository;

    @InjectMocks
    LinkedInAuthorizationFilter filter;

    MockHttpServletResponse response ;
    MockHttpServletRequest request;
    FilterChain chain;
    Connection<LinkedIn> connection;

    @Before
    public void setup() {
        initMocks(this);

        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        chain = mock(FilterChain.class);
        connection = mock(Connection.class);
    }

    @Test
    public void authorizationNeededResponse() throws IOException {
        filter.authorizationNeeded(response);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("application/json",response.getContentType());

        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode node = factory.objectNode();
        node.put("message", "No connection - authorization with linkedIn needed");
        assertEquals(node.toString(),response.getContentAsString());
    }

    @Test
    public void noPrimaryConnection() throws IOException, ServletException {
        filter.doFilter(request,response,chain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());

        verify(repository).findPrimaryConnection(LinkedIn.class);
        verifyZeroInteractions(repository);
        verifyZeroInteractions(connection);
    }


    @Test
    public void connectionIsExpired() throws IOException, ServletException {
        when(repository.findPrimaryConnection(LinkedIn.class)).thenReturn(connection);
        when (connection.hasExpired()).thenReturn(true);

        filter.doFilter(request,response,chain);

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        verify(repository).findPrimaryConnection(LinkedIn.class);
        verify(connection).hasExpired();
        verify(connection).refresh();
        verifyZeroInteractions(repository);
        verifyZeroInteractions(connection);
    }

    @Test(expected = NestedServletException.class)
    public void unexpectedException() throws IOException, ServletException {
        when(repository.findPrimaryConnection(LinkedIn.class)).thenReturn(connection);
        when(connection.hasExpired()).thenReturn(false);

        doThrow(NestedServletException.class).when(chain).doFilter(request, response);

        filter.doFilter(request, response, chain);
    }

    @Test
    public void unauthorized() throws IOException, ServletException {
        when(repository.findPrimaryConnection(LinkedIn.class)).thenReturn(connection);
        when (connection.hasExpired()).thenReturn(false);
        doThrow(new NestedServletException("test", new NotAuthorizedException("linkedIn", "not authorized"))).when(chain).doFilter(request, response);

        filter.doFilter(request, response, chain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());

        verify(repository).findPrimaryConnection(LinkedIn.class);
        verify(connection).hasExpired();
        verify(connection).getKey();
        verify(repository).removeConnection(any(ConnectionKey.class));
        verifyZeroInteractions(repository);
        verifyZeroInteractions(connection);
    }

}
