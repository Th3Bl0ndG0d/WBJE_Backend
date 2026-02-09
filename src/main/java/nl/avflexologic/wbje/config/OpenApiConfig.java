////package nl.avflexologic.wbje.config;
////
////import io.swagger.v3.oas.models.Components;
////import io.swagger.v3.oas.models.OpenAPI;
////import io.swagger.v3.oas.models.info.Contact;
////import io.swagger.v3.oas.models.info.Info;
////import io.swagger.v3.oas.models.info.License;
////import io.swagger.v3.oas.models.security.*;
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import java.util.Map;
////
////@Configuration
////
////public class OpenApiConfig {
////
////    @Bean
////    public OpenAPI wbjeOpenApi(
////            @Value("${keycloak.client-id}") String clientId,
////            @Value("${keycloak.client-secret}") String clientSecret,
////            @Value("${keycloak.realm}") String realm,
////            @Value("${keycloak.auth-server}") String authServer
////    ) {
////
////        // Token URL bepalen
////        String tokenUrl = authServer.replace("auth", "token");
////
////        // HTML-description opbouwen
////        String oauthDescription =
////                "Keycloak OAuth2 login for WBJE.<br><br>" +
////                        "<b>client_id:</b> " + clientId + "<br>" +
////                        "<b>client_secret:</b> " + clientSecret + "<br><br>" +
////                        "Copy/paste these values into the fields above in case Swagger cannot auto-fill them.";
////
////        OpenAPI openApi = new OpenAPI()
////                .info(new Info()
////                        .title("WBJE API")
////                        .version("v1.0.0")
////                        .description("REST API for the Web Based Job Editor.")
////                        .contact(new Contact().name("AV Flexologic"))
////                        .license(new License().name("Proprietary"))
////                )
////                .components(new Components()
////                        .addSecuritySchemes("keycloak", new SecurityScheme()
////                                .type(SecurityScheme.Type.OAUTH2)
////                                .description(oauthDescription)   // ← jouw gewenste beschrijving
////                                .flows(new OAuthFlows()
////                                        .authorizationCode(new OAuthFlow()
////                                                .authorizationUrl(authServer)
////                                                .tokenUrl(tokenUrl)
////                                                .scopes(new Scopes()
////                                                        .addString("openid", "OpenID Connect scope")
////                                                )
////                                        )
////                                )
////                        )
////                )
////                .addSecurityItem(new SecurityRequirement().addList("keycloak"));
////
////        // Swagger UI automatisch invullen
////        openApi.addExtension("x-swagger-ui-init-oauth", Map.of(
////                "clientId", clientId,
////                "clientSecret", clientSecret,
////                "usePkceWithAuthorizationCodeGrant", true
////        ));
////
////        return openApi;
////    }
////}
//package nl.avflexologic.wbje.config;
//
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
//
//// MODEL-OBJECTEN (voor Keycloak OAuth2)
//import io.swagger.v3.oas.models.Components;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Contact;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.info.License;
//import io.swagger.v3.oas.models.security.OAuthFlow;
//import io.swagger.v3.oas.models.security.OAuthFlows;
//import io.swagger.v3.oas.models.security.Scopes;
//import io.swagger.v3.oas.models.security.SecurityRequirement;
//import io.swagger.v3.oas.models.security.SecurityScheme;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Map;
//
//@Configuration
//@io.swagger.v3.oas.annotations.security.SecurityScheme(
//        name = "bearerAuth",
//        type = SecuritySchemeType.HTTP,
//        scheme = "bearer",
//        bearerFormat = "JWT"
//)
//public class OpenApiConfig {
//
//    @Bean
//    public OpenAPI wbjeOpenApi(
//            @Value("${keycloak.client-id}") String clientId,
//            @Value("${keycloak.client-secret}") String clientSecret,
//            @Value("${keycloak.realm}") String realm,
//            @Value("${keycloak.auth-server}") String authServer   // ← moet de realm-base-URL zijn
//    ) {
//
//        // 🟩 Correcte Keycloak OAuth2 endpoints
//        String authorizationUrl = authServer + "/protocol/openid-connect/auth";
//        String tokenUrl = authServer + "/protocol/openid-connect/token";
//
//        // HTML-description opbouwen
//        String oauthDescription =
//                "Keycloak OAuth2 login for WBJE.<br><br>" +
//                        "<b>client_id:</b> " + clientId + "<br>" +
//                        "<b>client_secret:</b> " + clientSecret + "<br><br>" +
//                        "Copy/paste these values into the fields above in case Swagger cannot auto-fill them.";
//
//        OpenAPI openApi = new OpenAPI()
//                .info(new Info()
//                        .title("WBJE API")
//                        .version("v1.0.0")
//                        .description("REST API for the Web Based Job Editor.")
//                        .contact(new Contact().name("AV Flexologic"))
//                        .license(new License().name("Proprietary"))
//                )
//                .components(new Components()
//                        .addSecuritySchemes("keycloak",
//                                new SecurityScheme()
//                                        .type(SecurityScheme.Type.OAUTH2)
//                                        .description(oauthDescription)
//                                        .flows(new OAuthFlows()
//                                                .authorizationCode(
//                                                        new OAuthFlow()
//                                                                .authorizationUrl(authorizationUrl)
//                                                                .tokenUrl(tokenUrl)
//                                                                .scopes(new Scopes()
//                                                                        .addString("openid", "OpenID Connect scope")
//                                                                )
//                                                )
//                                        )
//                        )
//                )
//                .addSecurityItem(new SecurityRequirement().addList("keycloak"));
//
//        // Swagger UI automatisch invullen
//        openApi.addExtension("x-swagger-ui-init-oauth", Map.of(
//                "clientId", clientId,
//                "clientSecret", clientSecret,
//                "usePkceWithAuthorizationCodeGrant", true
//        ));
//
//        return openApi;
//    }
//}
package nl.avflexologic.wbje.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;

// Keycloak OAuth2 model classes
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@io.swagger.v3.oas.annotations.security.SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI wbjeOpenApi(
            @Value("${keycloak.client-id}") String clientId,
            @Value("${keycloak.client-secret}") String clientSecret,
            @Value("${keycloak.realm}") String realm,
            @Value("${keycloak.auth-server}") String authServer
    ) {

        // Correct Keycloak endpoints
        String authorizationUrl = authServer + "/protocol/openid-connect/auth";
        String tokenUrl = authServer + "/protocol/openid-connect/token";

        // OAuth description
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
                        .addSecuritySchemes("keycloak",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.OAUTH2)
                                        .description(oauthDescription)
                                        .flows(new OAuthFlows()
                                                .authorizationCode(
                                                        new OAuthFlow()
                                                                .authorizationUrl(authorizationUrl)
                                                                .tokenUrl(tokenUrl)
                                                                .scopes(new Scopes()
                                                                        .addString("openid", "OpenID Connect scope")
                                                                        // ⬇️ MINIMALE wijziging: rollen als scopes toegevoegd
                                                                        .addString("service", "Full administrative access (maps to ROLE_ADMIN)")
                                                                        .addString("operator", "Standard operational access (maps to ROLE_USER)")

                                                                )
                                                )
                                        )
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("keycloak"));

        // Swagger UI autofill
        openApi.addExtension("x-swagger-ui-init-oauth", Map.of(
                "clientId", clientId,
                "clientSecret", clientSecret,
                "usePkceWithAuthorizationCodeGrant", true
        ));

        return openApi;
    }
}
