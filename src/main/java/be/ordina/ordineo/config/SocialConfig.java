package be.ordina.ordineo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by gide on 17/03/16.
 */
@Configuration
public class SocialConfig extends SocialConfigurerAdapter {

    @Override
    public UserIdSource getUserIdSource() {
        return new SessionUsernameSource();
    }

    private static final class SessionUsernameSource implements UserIdSource {
        @Override
        public String getUserId() {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

            String userId = (String) request.getSession().getAttribute("username");

            if (StringUtils.isEmpty(userId)) {
                userId = request.getParameter("username");
                request.getSession().setAttribute("username", userId);
            }

            return userId;
        }
    }

    @Bean
    @Primary
    @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
    public ConnectionRepository connectionRepository(UsersConnectionRepository usersConnectionRepository) {
        return usersConnectionRepository.createConnectionRepository(getUserIdSource().getUserId());
    }
}
