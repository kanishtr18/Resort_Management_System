package com.resortmanagement.system.security.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.resortmanagement.system.security.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    // FIX: Externalize CORS origins — add to application.properties:
    // cors.allowed-origins=http://localhost:5173,https://yourprod.com
    @Value("${cors.allowed-origins:http://localhost:5173}")
    private List<String> allowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers("/api/auth/**").permitAll()

                                // ADMIN only
                                .requestMatchers("/api/hr/**", "/api/reporting/**")
                                    .hasAuthority("ADMIN")

                                // EMPLOYEE + ADMIN
                                // FIX 1: /api/bookings/** (plural) added — ReservationRoomAssignmentController uses this path
                                // FIX 2: /api/fnb/** added — MenuItemIngredientController and OrderItemController are unversioned
                                // FIX 3: /api/room-blocks/**, /api/room-amenities/** added — were falling to anyRequest with no role check
                                // FIX 4: /api/maintenance/** added — MaintenanceRequestController was unprotected
                                // FIX 5: /api/communications/** added — CommunicationController was unprotected
                                .requestMatchers(
                                        "/api/billing/**",
                                        "/api/booking/**",
                                        "/api/bookings/**",
                                        "/api/housekeeping/**",
                                        "/api/housekeeping",
                                        "/api/room-types/**",
                                        "/api/room-types",
                                        "/api/rooms/**",
                                        "/api/rooms",
                                        "/api/room-blocks/**",
                                        "/api/room-amenities/**",
                                        "/api/amenities/**",
                                        "/api/amenities",
                                        "/api/maintenance/**",
                                        "/api/inventory/**",
                                        "/api/fnb/**",
                                        "/api/rate-plans/**",
                                        "/api/rate-history/**",
                                        "/api/pricing/**",
                                        "/api/marketing/**",
                                        "/api/communications/**",
                                        "/api/guests/**",
                                        "/api/guests"
                                ).hasAnyAuthority("EMPLOYEE", "ADMIN")

                                // GUEST + EMPLOYEE + ADMIN
                                .requestMatchers("/api/support/**", "/api/feedback/**")
                                    .hasAnyAuthority("GUEST", "EMPLOYEE", "ADMIN")

                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}