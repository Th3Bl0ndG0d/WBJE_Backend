package nl.avflexologic.wbje.config;//package com.example.les18.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfigBasic {
//
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService(){
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        UserDetails user1 = User.withUsername("mark").password(passwordEncoder().encode("geheim")).roles("USER").build();
//        UserDetails user2 = User.withUsername("nova").password(passwordEncoder().encode("novi")).roles("ADMIN").build();
//        manager.createUser(user1);
//        manager.createUser(user2);
//        return manager;
//    }
//
//    @Bean
//    public SecurityFilterChain config(HttpSecurity http) throws Exception{
//        return http
//                .httpBasic(Customizer.withDefaults())
//                .authorizeHttpRequests((auth) -> auth
//                        .requestMatchers("/private").authenticated()
//                        .requestMatchers("/public").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/admin").hasAuthority("ROLE_ADMIN")
//                )
//                .build();
//    }
//}
