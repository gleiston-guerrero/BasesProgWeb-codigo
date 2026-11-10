// Archivo: src/main/java/com/comercializadora/sgpc/config/SeguridadConfig.java
package com.comercializadora.sgpc.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.*;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity                 // activa @PreAuthorize
public class SeguridadConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http,
            JwtAuthenticationConverter conversorRoles) throws Exception {
        http
            // API sin cookies: el token viaja en la cabecera Authorization
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Las páginas de error muestran el código real (400, 404...)
                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/actuator/health/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**",
                    "/swagger-ui.html", "/cliente.html").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
                .requestMatchers("/api/**").authenticated()
                .anyRequest().authenticated())
            // Servidor de recursos: valida "Authorization: Bearer <jwt>"
            .oauth2ResourceServer(o -> o
                .jwt(jwt -> jwt.jwtAuthenticationConverter(conversorRoles)));
        return http.build();
    }

    // Claim "roles": ["ADMIN"] -> autoridad ROLE_ADMIN
    @Bean
    JwtAuthenticationConverter conversorRoles() {
        JwtGrantedAuthoritiesConverter roles = new JwtGrantedAuthoritiesConverter();
        roles.setAuthoritiesClaimName("roles");
        roles.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter conversor = new JwtAuthenticationConverter();
        conversor.setJwtGrantedAuthoritiesConverter(roles);
        return conversor;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();          // factor de trabajo 10
    }

    // Lo usa AuthController: usuario de la BD + comparación BCrypt
    @Bean
    AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService, PasswordEncoder encoder) {
        DaoAuthenticationProvider proveedor =
            new DaoAuthenticationProvider(userDetailsService);
        proveedor.setPasswordEncoder(encoder);
        return new ProviderManager(proveedor);
    }
}
