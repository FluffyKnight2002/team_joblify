package com.ace_inspiration.team_joblify.controller.hr;


import com.ace_inspiration.team_joblify.entity.Role;
import com.ace_inspiration.team_joblify.service.hr_service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.Collection;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String showLoginForm(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // Get the user's authorities
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            // Check if any of the user's authorities match any role in the UserRole Enum
            boolean hasUserRole = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(role -> Arrays.stream(Role.values())
                            .anyMatch(userRole -> userRole.name().equals(role)));

            if (hasUserRole) {
                return "redirect:/dashboard";
            }
        }


        // User is not authenticated or has different authorities, show the login page
        return "login";
    }

    @GetMapping("/user-register")
    public String showUserRegisterForm(){

        return "user-register";
    }

    @GetMapping("/all-user-list")
    public String showAllUsersList(){
        return "all-user-list";
    }

    @GetMapping("/user-profile-edit")
    public String showUserProfileEdit(){
        return "user-profile-edit";
    }

    @GetMapping("/password-change")
    public String showPasswordChangeForm() {
        return "password-change";
    }

    @GetMapping("/forgot-password-form")
    public String showForgetPasswordForm() {
        return "forgot-password";
    }

    @GetMapping("/otp-authentication-form")
    public String showOTPForm() {
        return "otp-authentication";
    }

    @GetMapping("/email-check-form")
    public String showEmailCheckForm() {
        return "email-check-for-otp";
    }


}
