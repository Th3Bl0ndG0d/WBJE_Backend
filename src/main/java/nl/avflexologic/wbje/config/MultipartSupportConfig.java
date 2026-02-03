package nl.avflexologic.wbje.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
public class MultipartSupportConfig {

    /**
     * Main multipart resolver for Spring Security.
     * It ensures that multipart requests are parsed lazily,
     * preventing early request wrapping that loses the Authorization header.
     */
    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
        resolver.setResolveLazily(true); // Belangrijk: multipart pas NA authentication verwerken
        return resolver;
    }

    /**
     * Ensures that Spring Boot uses its own multipart pipeline
     * instead of letting the underlying servlet container override it.
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        return new MultipartConfigElement(""); // Laat Spring bepalen hoe multipart wordt afgehandeld
    }

    /**
     * Ensures HiddenHttpMethodFilter is placed at the end of the filter chain.
     * Prevents early wrapping of the request that could remove headers.
     */
    @Bean
    public FilterRegistrationBean<HiddenHttpMethodFilter> hiddenHttpMethodFilter() {
        FilterRegistrationBean<HiddenHttpMethodFilter> registration =
                new FilterRegistrationBean<>(new HiddenHttpMethodFilter());

        registration.setOrder(Integer.MAX_VALUE); // Zéér laag in de chain
        return registration;
    }
}
