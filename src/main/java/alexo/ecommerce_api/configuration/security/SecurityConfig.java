package alexo.ecommerce_api.configuration.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /*
     * Main security configuration
     * Defines endpoint access rules and JWT filter setup
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  {
        http
                // Disable CSRF (not needed for stateless JWT)
                .csrf(AbstractHttpConfigurer::disable)

                // Configure endpoint authorization
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/**").permitAll()

                        // Role-based endpoints
//                        .requestMatchers("/auth/user/**").hasAuthority("ROLE_USER")
//                        .requestMatchers("/auth/admin/**").hasAuthority("ROLE_ADMIN")

                        // All other endpoints require authentication
//                        .anyRequest().authenticated()
                )

                // Stateless session (required for JWT)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                // Set custom authentication provider
//                .authenticationProvider(authenticationProvider())

                // Add JWT filter before Spring Security's default filter
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}