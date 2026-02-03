package nl.avflexologic.wbje.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SecurityController {

    @GetMapping("/public")
    public String publiek(){
        return "Dit is een publiek endpoint";
    }

    @GetMapping("/private")
    public String geheim(){
        return "Dit is een besloten endpoint";
    }

    @GetMapping("/admin")
    public String admin(){
        return "Dit endpoint is voor de ADMIN_ROL";
    }

    @GetMapping("/hello")
    public String hallo(Authentication authentication){
        String naam = authentication.getName();
        return "Hallo " + naam;
    }
    @GetMapping("/roles")
    public List<String> roles(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .toList();
    }
    @GetMapping("/debug-auth")
    public Object debugAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


}
