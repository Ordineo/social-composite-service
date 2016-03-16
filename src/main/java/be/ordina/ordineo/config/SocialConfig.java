package be.ordina.ordineo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;

import javax.inject.Inject;

/**
 * Created by Hans on 16/03/16.
 */

@Configuration
public class SocialConfig {

    @Inject
    Environment environment;

    @Bean
    public ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new LinkedInConnectionFactory(
                environment.getProperty("linkedin.consumerKey"),
                environment.getProperty("linkedin.consumerSecret")));
        return registry;
    }

    @Bean
    @Scope(value="request", proxyMode= ScopedProxyMode.INTERFACES)
    public LinkedIn linkedIn() {
        return connectionRepository().getPrimaryConnection(LinkedIn.class).getApi();
    }


}
