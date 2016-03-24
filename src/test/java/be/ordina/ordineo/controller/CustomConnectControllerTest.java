package be.ordina.ordineo.controller;

import be.ordina.ordineo.filter.LinkedInConnectionFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by gide on 24/03/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RequestContextHolder.class, ServletRequestAttributes.class })
public class CustomConnectControllerTest {

    @Mock
    ConnectionFactoryLocator connectionFactoryLocator;

    @Mock
    ConnectionRepository connectionRepository;

    @InjectMocks
    CustomConnectController controller;

    MockHttpServletRequest request;
    ServletRequestAttributes attributes;

    @Before
    public void setup() {
        initMocks(this);

        mockStatic(RequestContextHolder.class);

        request = new MockHttpServletRequest();
        attributes = PowerMockito.mock(ServletRequestAttributes.class);

        when(RequestContextHolder.currentRequestAttributes()).thenReturn(attributes);
        when(attributes.getRequest()).thenReturn(request);
    }

    @Test
    public void connectedViewOriginal() {
        request.getSession().setAttribute(LinkedInConnectionFilter.ORGINAL_URL, "test");
        assertEquals("redirect:test", controller.connectedView(""));
    }

    @Test
    public void connectedViewSuper() {
        assertEquals("connect/Connected", controller.connectedView(""));
    }

}