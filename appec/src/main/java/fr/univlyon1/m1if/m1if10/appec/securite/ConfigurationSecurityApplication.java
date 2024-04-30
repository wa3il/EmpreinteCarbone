package fr.univlyon1.m1if.m1if10.appec.securite;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Security configuration for the application.
 */
@Configuration
@EnableWebSecurity
public class ConfigurationSecurityApplication {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public ConfigurationSecurityApplication(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }


    @SuppressWarnings("deprecation")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.applyPermitDefaultValues();
                    configuration.addAllowedMethod(HttpMethod.PUT); // Ajout de la méthode PUT
                    configuration.addAllowedMethod(HttpMethod.DELETE); // Ajout de la méthode DELETE
                    return configuration;
                }))
                .authorizeRequests(authorize -> authorize
                        .requestMatchers(POST, "/users").permitAll()
                        .requestMatchers(POST, "/users/login").permitAll()
                        .requestMatchers(GET, "/aliments").permitAll()
                        .requestMatchers(GET, "/aliments/**").permitAll()
                        .requestMatchers( "/swagger-ui.html").permitAll()
                        .requestMatchers( "/swagger-ui/**").permitAll()
                        .requestMatchers( "/v3/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    

}
