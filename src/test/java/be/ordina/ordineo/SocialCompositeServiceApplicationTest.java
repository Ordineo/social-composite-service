package be.ordina.ordineo;

import be.ordina.ordineo.filter.LinkedInAuthorizationFilter;
import be.ordina.ordineo.filter.LinkedInConnectionFilter;
import be.ordina.ordineo.filter.SimpleCORSFilter;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.Filter;
import javax.servlet.ServletContext;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by gide on 24/03/16.
 */
public class SocialCompositeServiceApplicationTest {

    SocialCompositeServiceApplication application;

    @Before
    public void setup() {
        application = new SocialCompositeServiceApplication();
    }

    @Test
    public void testLinkedInAuthorizationFilterRegistration() throws Exception {
        FilterRegistrationBean registration = application.linkedInAuthorizationFilterRegistration();

        assertEquals(1, registration.getUrlPatterns().size());
        assertEquals("/api/*", registration.getUrlPatterns().iterator().next());

        ServletContext servletContext = mock(ServletContext.class);
        registration.onStartup(servletContext);

        verify(servletContext).addFilter(anyString(), any(LinkedInAuthorizationFilter.class));
    }

    @Test
    public void testLinkedInConnectionFilterRegistration() throws Exception {
        FilterRegistrationBean registration = application.linkedInConnectionFilterRegistration();

        assertEquals(1, registration.getUrlPatterns().size());
        assertEquals("/connect/linkedin", registration.getUrlPatterns().iterator().next());

        ServletContext servletContext = mock(ServletContext.class);
        registration.onStartup(servletContext);

        verify(servletContext).addFilter(anyString(), any(LinkedInConnectionFilter.class));
    }

    @Test
    public void testSimpleCORSFilterRegistration() throws Exception {
        FilterRegistrationBean registration = application.simpleCORSFilterRegistration();

        assertEquals(1, registration.getUrlPatterns().size());
        assertEquals("/*", registration.getUrlPatterns().iterator().next());

        ServletContext servletContext = mock(ServletContext.class);
        registration.onStartup(servletContext);

        verify(servletContext).addFilter(anyString(), any(SimpleCORSFilter.class));
    }

}