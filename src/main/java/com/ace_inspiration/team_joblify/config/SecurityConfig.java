package com.ace_inspiration.team_joblify.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    @Value("${app.remember-me-key}")
    private static String rememberMeKey;
    private final MyUserDetailsService myUserDetailsService;

    @Bean
    public CustomAccessDeniedHandler deniedHandler(){
        return new CustomAccessDeniedHandler();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(
                        csrf->csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .rememberMe(
                        rememberMe -> rememberMe
                                .key(rememberMeKey)
                                .tokenValiditySeconds(84600)
                                .rememberMeCookieName("cookie")
                                .rememberMeParameter("remember-me")
                                .userDetailsService(myUserDetailsService)
                )
                .authorizeHttpRequests(authorize->authorize
                        .requestMatchers("/assets/**", "/assets/css/**", "/assets/images/**", "/assets/js/**", "/assets/vendors/**").permitAll()
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception-> exception
                        .accessDeniedHandler(deniedHandler())
                )

                .formLogin(login->login
                        .loginPage("/login")
                        .usernameParameter("username")
                        .failureUrl("/?error=true")
                        .defaultSuccessUrl("/dashboard?loginSuccess=true")
                        .permitAll()
                )
                .logout(logout->logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?success=true")
                        .deleteCookies("JSESSIONID", "remember-me-cookie")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .permitAll()
                );


        return http.build();
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
