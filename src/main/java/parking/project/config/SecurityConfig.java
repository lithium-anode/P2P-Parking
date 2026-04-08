package parking.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * [Design Pattern: Framework Pattern - Security Filter Chain]
 * This configuration defines the primary security architecture enforced by the 
 * Spring Framework, acting as the global gatekeeper for the system.
 * * [Goal Alignment: Assigned Role Access]
 * Implements the requirement that access to specific functionalities 
 * depends on the user's role (Driver, Owner, or Admin).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * [Goal Alignment: Authentication]
     * Configures the BCrypt password encoder to ensure that user credentials 
     * are stored and verified securely.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * [Goal Alignment: Role-Based Access Control]
     * Defines which URL patterns are accessible to each user role.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Publicly accessible paths for registration and login
                .requestMatchers("/", "/register", "/login", "/error", "/css/**", "/js/**").permitAll()
                
                // Administrator higher-level control 
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // Space Owner listing and rental management [cite: 4, 10]
                .requestMatchers("/owner/**").hasRole("OWNER")
                
                // Driver search, reservation, and payment [cite: 3, 7]
                .requestMatchers("/driver/**").hasRole("DRIVER")
                
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/default", true) // Logic to redirect based on role
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}