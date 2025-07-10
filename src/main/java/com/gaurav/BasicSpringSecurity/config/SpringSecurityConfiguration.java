package com.gaurav.BasicSpringSecurity.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(authentication -> authentication
                .requestMatchers("/api/public/**").permitAll());
        httpSecurity.authorizeHttpRequests(authentication -> authentication
                .requestMatchers("/api/admin/**").hasRole("ADMIN").anyRequest().authenticated());
        httpSecurity.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.httpBasic(Customizer.withDefaults());
        httpSecurity.exceptionHandling(exception -> exception
                //for unauthorized access
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Unauthorized - Please login first.\"}");
                })
                //for forbidden access
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Forbidden - You do not have permission.\"}");
                })
        );
        return httpSecurity.build();
    }
    //password encoder to encode the password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //✅ InMemoryUserDetailsManager is used to define users in memory (not in a database) for quick testing, demos, or small apps.
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username("gauravsoni")
                .password(passwordEncoder.encode("gauravsoni123"))
                .roles("ADMIN","USER")
                .build();
        UserDetails user = User.builder()
                .username("garimasoni")
                .password(passwordEncoder.encode("garimasoni123"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(admin,user);
    }
}
