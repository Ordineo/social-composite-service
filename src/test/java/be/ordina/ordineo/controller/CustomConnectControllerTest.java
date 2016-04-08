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
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.view.RedirectView;


import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
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
    public void oauth2ErrorCallbackOriginal() throws Exception {
        request.getSession().setAttribute(LinkedInConnectionFilter.ORGINAL_URL, "test");
        RedirectView view = controller.oauth2ErrorCallback(null, null, "description", null, null);
        assertEquals("test?status=401&error_description=description", view.getUrl());
    }

    @Test
    public void oauth2ErrorCallbackSuper() throws Exception {
        SessionStrategy sessionStrategy = mock(SessionStrategy.class);
        controller.setSessionStrategy(sessionStrategy);

        NativeWebRequest request = mock(NativeWebRequest.class);
        when(request.getNativeRequest(HttpServletRequest.class)).thenReturn( this.request );

        RedirectView view = controller.oauth2ErrorCallback("linkedin", null, "description", null, request);
        assertEquals("/connect/linkedin", view.getUrl());
    }

    @Test
    public void connectedViewOriginal() {
        request.getSession().setAttribute(LinkedInConnectionFilter.ORGINAL_URL, "test");
        assertEquals("redirect:test?status=200", controller.connectedView(""));
    }

    @Test
    public void connectedViewSuper() {
        assertEquals("connect/Connected", controller.connectedView(""));
    }
}