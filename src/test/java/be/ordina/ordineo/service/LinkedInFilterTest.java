package be.ordina.ordineo.service;

import be.ordina.ordineo.filter.LinkedInConnectionFilter;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.linkedin.api.LinkedIn;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Hans on 23/03/16.
 */
public class LinkedInFilterTest {

  private  Connection<LinkedIn> connection = mock(Connection.class);
    @Mock
    ConnectionRepository repository;
    @Mock
    HttpServletResponse response;


    @InjectMocks
    LinkedInConnectionFilter filter;


    @Before
    public void setup() {
        initMocks(this);
    }




    @Test
    public void authorizationNeededResponseTest() throws IOException {

        Assert.assertEquals(filter.authorizationNeeded(response),"message", "No connection - authorization with linkedIn needed");

    }

    @Test
    public void testFindPrimaryConnection() {

        LinkedIn linkedin = mock(LinkedIn.class);
        when(repository.findPrimaryConnection(LinkedIn.class)).thenReturn(connection);


    }

    @Test
    public void testWhenConnectionIsNull(){
        when(repository.findPrimaryConnection(LinkedIn.class)).thenReturn(null);

    }

    @Test
    public void testWhenConnectionIsExpired(){

        when(repository.findPrimaryConnection(LinkedIn.class).hasExpired()).then(connection.refresh());
    }

    @Test




//        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//            Connection<LinkedIn> primaryConnection = connectionRepository.findPrimaryConnection(LinkedIn.class);
//
//            HttpServletResponse response = (HttpServletResponse)servletResponse;
//
//            if (primaryConnection == null) {
//                authorizationNeeded(response);
//                return;
//            }
//
//            if ( primaryConnection.hasExpired()) {
//                primaryConnection.refresh();
//            }
//
//            try {
//                filterChain.doFilter(servletRequest, servletResponse);
//            } catch (NotAuthorizedException e) {
//                connectionRepository.removeConnection( primaryConnection.getKey() );
//
//                authorizationNeeded(response);
//                return;
//            }
//        }



    }
