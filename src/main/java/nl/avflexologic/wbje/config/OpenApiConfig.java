package nl.avflexologic.wbje.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Central OpenAPI configuration for the WBJE REST API.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI wbjeOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("WBJE API")
                        .version("v1.0.0")
                        .description("REST API for the Web Based Job Editor.")
                        .contact(new Contact()
                                .name("AV Flexologic")
                                .email("michel_karsten@hotmail.com"))
                        .license(new License()
                                .name("Proprietary")
                                .url("https://www.flexologic.nl")));
    }
}
