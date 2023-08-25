package com.ace_inspiration.team_joblify.config;

import lombok.RequiredArgsConstructor;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.ace_inspiration.team_joblify.entity.Role;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${app.remember.me.key}")
    private String rememberMeKey;

    private final MyUserDetailsService myUserDetailsService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(
                        csrf->csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .rememberMe(
                        rememberMe -> rememberMe
                                .rememberMeServices(customRememberMeServices())
                                .key(rememberMeKey)
                                // .tokenValiditySeconds(84600)
                                // .rememberMeCookieName("remember-me-cookie")
                                // .rememberMeParameter("remember-me")
                                // .userDetailsService(myUserDetailsService)
                )
                .authorizeHttpRequests(authorize->authorize
                .requestMatchers("/all-user-list").hasAuthority(Role.DEFAULT_HR.name())
                        .requestMatchers("/assets/**",
                                "/assets/css/**",
                                "/assets/images/**",
                                "/assets/js/**",
                                "/assets/vendors/**").permitAll()
                        .requestMatchers("/**", "/ws/**").permitAll()
                        
                        .anyRequest().authenticated()
                )
                

                .formLogin(login->login
                        .loginPage("/login")
                        .usernameParameter("username")
                        .failureHandler(customAuthenticationFailureHandler()) // Set the custom failure handler
                        .defaultSuccessUrl("/dashboard?loginSuccess=true")
                        .permitAll()
                )
                .logout(logout->logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logoutSuccess=true")
                        .deleteCookies("JSESSIONID", "remember-me-cookie")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .permitAll()
                );


        return http.build();
    }

    @Bean
    public CustomRememberMeServices customRememberMeServices() {
        return new CustomRememberMeServices(rememberMeKey, myUserDetailsService);
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
