package com.resortmanagement.system.security.config;

import com.resortmanagement.system.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers("/api/auth/**").permitAll()
                               // HR and Reporting are strictly for ADMINs and Managers
                                .requestMatchers("/api/hr/**", "/api/reporting/**").hasAuthority("ADMIN")
                               // Internal modules are for EMPLOYEE and ADMIN
                                .requestMatchers("/api/billing/**", "/api/booking/**", "/api/v1/inventory/**", "/api/v1/fnb/**", "/api/v1/rate-**", "/api/v1/pricing/**", "/api/marketing/**", "/api/room**", "/api/amenities").hasAnyAuthority("EMPLOYEE", "ADMIN")
                               // Guests can access support, feedback, and their own bookings (if specific endpoints permit, for now default to accessible by all authenticated users or specific guest matchers)
                                .requestMatchers("/api/support/**", "/api/feedback/**").hasAnyAuthority("GUEST", "EMPLOYEE", "ADMIN")
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
