package nl.avflexologic.wbje.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI wbjeOpenApi(
            @Value("${keycloak.client-id}") String clientId,
            @Value("${keycloak.client-secret}") String clientSecret,
            @Value("${keycloak.realm}") String realm,
            @Value("${keycloak.auth-server}") String authServer
    ) {

        // Token URL bepalen
        String tokenUrl = authServer.replace("auth", "token");

        // HTML-description opbouwen
        String oauthDescription =
                "Keycloak OAuth2 login for WBJE.<br><br>" +
                        "<b>client_id:</b> " + clientId + "<br>" +
                        "<b>client_secret:</b> " + clientSecret + "<br><br>" +
                        "Copy/paste these values into the fields above in case Swagger cannot auto-fill them.";

        OpenAPI openApi = new OpenAPI()
                .info(new Info()
                        .title("WBJE API")
                        .version("v1.0.0")
                        .description("REST API for the Web Based Job Editor.")
                        .contact(new Contact().name("AV Flexologic"))
                        .license(new License().name("Proprietary"))
                )
                .components(new Components()
                        .addSecuritySchemes("keycloak", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .description(oauthDescription)   // ← jouw gewenste beschrijving
                                .flows(new OAuthFlows()
                                        .authorizationCode(new OAuthFlow()
                                                .authorizationUrl(authServer)
                                                .tokenUrl(tokenUrl)
                                                .scopes(new Scopes()
                                                        .addString("openid", "OpenID Connect scope")
                                                )
                                        )
                                )
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("keycloak"));

        // Swagger UI automatisch invullen
        openApi.addExtension("x-swagger-ui-init-oauth", Map.of(
                "clientId", clientId,
                "clientSecret", clientSecret,
                "usePkceWithAuthorizationCodeGrant", true
        ));

        return openApi;
    }
}
