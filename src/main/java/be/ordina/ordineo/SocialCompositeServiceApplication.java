package be.ordina.ordineo;

import be.ordina.ordineo.filter.LinkedInConnectionFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;

@SpringBootApplication
public class SocialCompositeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialCompositeServiceApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean linkedInConnectionFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(linkedInConnectionFilter());
		registration.addUrlPatterns("/api/*");
		registration.setName("linkedInConnectionFilter");
		return registration;
	}

	@Bean
	public Filter linkedInConnectionFilter() {
		return new LinkedInConnectionFilter();
	}
}
