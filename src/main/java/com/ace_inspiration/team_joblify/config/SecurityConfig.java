package com.ace_inspiration.team_joblify.config;

import com.ace_inspiration.team_joblify.entity.Role;
import lombok.RequiredArgsConstructor;
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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

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
                                
                )
                .authorizeHttpRequests(authorize->authorize
                
                        .requestMatchers("/assets/**",
                                "/assets/css/**",
                                "/assets/images/**",
                                "/assets/js/**",
                                "/assets/vendors/**").permitAll()

                        .requestMatchers("/ws/**", "/change-password", "/sendOTP", "/otp-submit", "/search-email",
                        "/find-phonenumber-by-email", "/error", "/vacancy/show-last", "/vacancy/show-all", "/vacancy/show-others",
                        "/vacancy/job-detail", "/vacancy/filter", "/", "/contact-us", "/job-detail", "/all-jobs", "/400", "/401", "/403",
                        "/404", "/405", "/500", "502", "/503", "/504", "/apply-job", "/titles", "/departments", "/fetch-titles", "/fetch-departments",
                        "/fetch-address", "/login", "/password-change", "/forgot-password-form", "/otp-authentication-form", "/email-check-form"
                        ).permitAll()

                        .requestMatchers("/user-register", "/all-user-list", "/get-all-user", "/send-invite-email", "/send-offer-mail",
                        "/suspend", "/activate", "/changeInterview").hasAnyAuthority(Role.DEFAULT_HR.name(), Role.SENIOR_HR.name())

                        .requestMatchers("/show-upload-vacancy-form", "/upload-vacancy", "/reopen-vacancy", "/reopen-vacancy-by-id", "/update-vacancy", "/close-vacancy"
                        
                        ).hasAnyAuthority(Role.DEFAULT_HR.name(), Role.SENIOR_HR.name(), Role.JUNIOR_HR.name())
                        
                        .requestMatchers("/vacancy/count", "/vacancy/show-all-data", "/allCandidate", "/allPositions", "/changStatus",
                        "/seeMore", "/dashboard", "/view-summaryinfo", "/process", "/getYear", "/interview-process", "/candidate-view-summary",
                        "/user-profile-edit", "/show-all-vacancies-page", "/all_candidates/**", "/interview_process/**", "/notifications/**", "/yearly-vacancy-count",
                        "/all-department", "/chart"
                        ).authenticated()

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
