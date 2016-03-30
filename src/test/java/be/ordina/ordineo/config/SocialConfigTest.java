package be.ordina.ordineo.config;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.social.UserIdSource;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.sql.DataSource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by gide on 24/03/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RequestContextHolder.class, ServletRequestAttributes.class })
public class SocialConfigTest {

    @Mock
    DataSource dataSource;

    @InjectMocks
    SocialConfig config;

    UserIdSource source;
    MockHttpServletRequest request;
    ServletRequestAttributes attributes;

    @Before
    public void setup() {
        initMocks(this);
        mockStatic(RequestContextHolder.class);

        source = config.getUserIdSource();
        request = new MockHttpServletRequest();
        attributes = PowerMockito.mock(ServletRequestAttributes.class);
    }

    @Test
    public void userIdSourceType() {
        assertEquals(SocialConfig.SessionUsernameSource.class, source.getClass());
    }

    @Test
    public void usersConnectionRepositoryType() {
        assertEquals(JdbcUsersConnectionRepository.class, config.getUsersConnectionRepository(null).getClass());
    }

    @Test
    public void getUserIdParameter() {
        when(RequestContextHolder.currentRequestAttributes()).thenReturn(attributes);
        when(attributes.getRequest()).thenReturn(request);

        request.addParameter("username", "Hans");

        assertEquals("hans", source.getUserId());
        assertEquals("Hans", request.getSession().getAttribute("username"));
    }

    @Test
    public void getUserIdSession() {
        when(RequestContextHolder.currentRequestAttributes()).thenReturn(attributes);
        when(attributes.getRequest()).thenReturn(request);

        request.getSession().setAttribute("username", "Hans");

        assertEquals("hans", source.getUserId());
    }

}