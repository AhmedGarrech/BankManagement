package com.garrech.bankmanagement.configurations;

import com.garrech.bankmanagement.utils.ClientType;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                        (authorizeRequests) -> authorizeRequests.requestMatchers("/login*").permitAll()
                                .requestMatchers("/api/clients**").hasRole(ClientType.ADMIN.name())
                                .requestMatchers("/api/operation**").hasAnyRole(ClientType.CLIENT.name(), ClientType.ADMIN.name())
                                .anyRequest().permitAll()
                )
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling
                                .accessDeniedPage("/access-denied")
                )
                .formLogin(withDefaults())
                .rememberMe(httpSecurityRememberMeConfigurer -> httpSecurityRememberMeConfigurer.alwaysRemember(true))
                .userDetailsService(userDetailsService)
                .csrf(AbstractHttpConfigurer::disable)
                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.
                        frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                        .disable())
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/h2-console"))
                .build();
    }
}
