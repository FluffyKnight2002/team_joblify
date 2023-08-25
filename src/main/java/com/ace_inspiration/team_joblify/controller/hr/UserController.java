package com.ace_inspiration.team_joblify.controller.hr;

import com.ace_inspiration.team_joblify.config.MyUserDetails;
import com.ace_inspiration.team_joblify.entity.Role;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Collection;

@Controller
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/login")
    public String showLoginForm() {
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
    public String showUserRegisterForm() {

        return "user-register";
    }

    @GetMapping("/all-user-list")
    public String showAllUsersList() {
        return "all-user-list";
    }

    @GetMapping("/user-profile-edit")
    public Object showUserProfileEdit(HttpServletResponse response, @RequestParam("email") String email,
            Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();

        if (myUserDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(Role.DEFAULT_HR.name()))
                || myUserDetails.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals(Role.SENIOR_HR.name()))) {
            return "user-profile-edit";
        } else if ((myUserDetails.getAuthorities().stream()
                .noneMatch(authority -> authority.getAuthority().equals(Role.DEFAULT_HR.name()))
                || myUserDetails.getAuthorities().stream()
                        .noneMatch(authority -> authority.getAuthority().equals(Role.SENIOR_HR.name())))
                && email.equals(myUserDetails.getEmail())) {
            return "user-profile-edit";
        } else {
            return "redirect:/403"; // Forward to the error controller
        }

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
