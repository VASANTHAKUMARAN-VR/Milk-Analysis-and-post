package com.milk.milkanalysis.config;

import com.milk.milkanalysis.model.User;
import com.milk.milkanalysis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;

    /**
     * ✅ Custom UserDetailsService that allows login using either email or mobile number.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return usernameOrMobile -> {
            var userOpt = userRepository.findByEmailOrMobileNumber(usernameOrMobile, usernameOrMobile);
            if (userOpt.isEmpty()) {
                throw new UsernameNotFoundException("User not found with email or mobile: " + usernameOrMobile);
            }

            User u = userOpt.get();
            // Always use email (or mobile if null) as username to ensure uniqueness
            String uniqueUsername = (u.getEmail() != null) ? u.getEmail() : u.getMobileNumber();

            UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(uniqueUsername)
                    .password(u.getPassword())
                    .disabled(!u.isEnabled())
                    .authorities(u.getRoles().stream().map(Enum::name).toArray(String[]::new));

            return builder.build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://localhost:3000",
                "https://cattle-enterprise.vercel.app/" // your Render URL
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/milk/**").permitAll()
                        .requestMatchers("/api/expense/**").permitAll()
                        .requestMatchers("/api/cow-purchase/**").permitAll()
                        .requestMatchers("/api/cow-sale/**").permitAll()
                        .requestMatchers("/api/cowrequests/**").permitAll()
                        .requestMatchers("/api/cow-sale-post/**").permitAll()
                        .requestMatchers("/api/market-post/**").permitAll()
                        .requestMatchers("/api/sale-records/**").permitAll()
                        .requestMatchers("/api/buy/**").permitAll()
                        .requestMatchers("/api/profit/**").permitAll()
                        .requestMatchers("/uploads/**", "/api/upload/**", "/api/files/**", "/api/images/**").permitAll()
                        .requestMatchers("/images/**", "/static/**", "/resources/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable());

        return http.build();
    }
}
