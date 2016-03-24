package be.ordina.ordineo.filter;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by gide on 24/03/16.
 */
public class LinkedInConnectionFilterTest {

    LinkedInConnectionFilter filter;
    MockHttpServletResponse response ;
    MockHttpServletRequest request;
    MockFilterChain chain;

    @Before
    public void setup() {
        filter = new LinkedInConnectionFilter();

        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        chain = new MockFilterChain();
    }

    @Test
    public void doNothingForGet() throws IOException, ServletException {
        request.setMethod("GET");

        filter.doFilter(request, response, chain);

        assertNull(request.getSession().getAttribute( LinkedInConnectionFilter.ORGINAL_URL ));
    }

    @Test
    public void saveUrlForPost() throws IOException, ServletException {
        request.setMethod("POST");
        request.addHeader("Referer", "testingUrl");

        filter.doFilter(request, response, chain);

        assertEquals("testingUrl", request.getSession().getAttribute( LinkedInConnectionFilter.ORGINAL_URL ));
    }

}