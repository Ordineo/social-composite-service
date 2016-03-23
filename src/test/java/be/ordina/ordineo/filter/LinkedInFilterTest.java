package be.ordina.ordineo.filter;

import be.ordina.ordineo.filter.LinkedInConnectionFilter;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.web.util.NestedServletException;
import sun.awt.image.ImageWatched;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Hans on 23/03/16.
 */
public class LinkedInFilterTest {

    @Mock
    ConnectionRepository repository;



    @InjectMocks
    LinkedInConnectionFilter filter;

    MockHttpServletResponse response ;
    MockHttpServletRequest request;
    MockFilterChain chain;
    Connection<LinkedIn> connection;
    @Before
    public void setup() {

        initMocks(this);
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        chain = new MockFilterChain();
        connection = mock(Connection.class);
    }




    @Test
    public void authorizationNeededResponseTest() throws IOException {
        filter.authorizationNeeded(response);
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("application/json",response.getContentType());

        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode node = factory.objectNode();
        node.put("message", "No connection - authorization with linkedIn needed");
        assertEquals(node.toString(),response.getContentAsString());


    }

    @Test
    public void testNoPrimaryConnection() throws IOException, ServletException {

        LinkedIn linkedin = mock(LinkedIn.class);
        filter.doFilter(request,response,chain);
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());

        verify(repository).findPrimaryConnection(LinkedIn.class);
        verifyZeroInteractions(repository);
        verifyZeroInteractions(connection);

    }


    @Test
    public void testWhenConnectionIsExpired() throws IOException, ServletException {

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

    @Test
    public void unAuthorized() throws IOException, ServletException {
        when(repository.findPrimaryConnection(LinkedIn.class)).thenReturn(connection);
        when (connection.hasExpired()).thenReturn(false);
        //when (chain.doFilter(request,response)).thenThrow(new NestedServletException("test"));


    }

}
