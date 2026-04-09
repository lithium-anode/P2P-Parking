package parking.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
    [Design Pattern: Behavioral - Chain of Responsibility]
    Pattern enforced by the SecurityFilterChain, which processes
    incoming requests through a series of filters to determine
    authentication and authorization, allowing for flexible handling
    of security concerns.
    Goal: Role-Based Access Control
*/
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Publicly accessible paths for registration and login
                .requestMatchers("/", "/register", "/login", "/error", "/css/**", "/js/**").permitAll()
                
                // Administrator higher-level control 
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // Space Owner listing and rental management
                .requestMatchers("/owner/**").hasRole("OWNER")
                
                // Driver search, reservation, and payment
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