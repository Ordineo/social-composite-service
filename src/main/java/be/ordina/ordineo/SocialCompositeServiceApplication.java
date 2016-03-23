package be.ordina.ordineo;

import be.ordina.ordineo.filter.LinkedInConnectionFilter;
import be.ordina.ordineo.filter.SimpleCORSFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;

import javax.servlet.Filter;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@EnableRetry
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
	public FilterRegistrationBean simpleCORSFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(simpleCORSFilter());
		registration.addUrlPatterns("/*");
		registration.setName("simpleCORSFilter");
		return registration;
	}

	@Bean
	public Filter linkedInConnectionFilter() {
		return new LinkedInConnectionFilter();
	}

	@Bean
	public Filter simpleCORSFilter() {return new SimpleCORSFilter();}
}
