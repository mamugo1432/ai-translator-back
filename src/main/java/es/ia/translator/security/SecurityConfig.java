package es.ia.translator.security;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.ia.translator.exceptions.ApiError;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer{

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5500","http://127.0.0.1:5500", "http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");

            ApiError error = new ApiError(
                    HttpStatus.FORBIDDEN.value(),
                    HttpStatus.FORBIDDEN.getReasonPhrase(),
                    List.of("No tienes permisos para acceder a este recurso"),
                    request.getRequestURI()
            );

            new ObjectMapper().writeValue(response.getOutputStream(), error);
        };
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {

        http
                .cors(cors->{})
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/users/check-username", "/users/check-email", "/stocks").permitAll()
                        .requestMatchers("/register", "/auth/login", "/verifyToken", "/error", "/translate").permitAll()
                        .requestMatchers("/users", "/users/editPassword/**").hasRole("ADMIN")
                        .requestMatchers("/users/edit/**", "/users/delete/**", "/users/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/advices/edit/**", "/advices/delete/**", "/advices/create/**","/advices/check-phrase").hasRole("ADMIN")
                        .requestMatchers("/advices", "/advices/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/authors", "/authors/**").hasRole("ADMIN")
                        .requestMatchers("/favorites-stocks","/favorites-stocks/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/favorites-authors","/favorites-authors/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/advices/check-phrase").hasRole("ADMIN")
                        .requestMatchers("/stock/edit/**", "/stock/create", "/stock/delete/**").hasRole("ADMIN")
                        .requestMatchers("/stock/**").hasAnyRole("USER", "ADMIN")
                        /* .requestMatchers(HttpMethod.GET, "/elements/**")
                        .requestMatchers(HttpMethod.POST, "/elements/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/elements/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/elements/**").hasRole("ADMIN")*/
                        .anyRequest().denyAll()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler())
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}