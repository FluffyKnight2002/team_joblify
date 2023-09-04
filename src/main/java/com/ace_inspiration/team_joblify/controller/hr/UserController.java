package com.ace_inspiration.team_joblify.controller.hr;

import com.ace_inspiration.team_joblify.config.MyUserDetails;
import com.ace_inspiration.team_joblify.entity.Role;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String showUserRegisterForm(Model model) {
        model.addAttribute("currentPage", "/user-register");
        return "user-register";
    }

    @GetMapping("/all-user-list")
    public String showAllUsersList(Model model) {
        model.addAttribute("currentPage", "/all-user-list");
        return "all-user-list";
    }

    @GetMapping("/user-profile-edit")
    public Object showUserProfileEdit(HttpServletResponse response, @RequestParam("email") String email,
            Authentication authentication, Model model) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        model.addAttribute("currentPage", "/user-profile-edit");
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
    public String showPasswordChangeForm(@RequestParam("email") String email, Authentication authentication) {
        
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();

        System.out.println(myUserDetails.getEmail() + email);

        if(myUserDetails.getEmail().equals(email)) {
            return "password-change";

        }
        return "redirect:/403";
    }

    @GetMapping("/forgot-password-form")
    public String showForgetPasswordForm(HttpSession session, Authentication authentication) {
        if (authentication.isAuthenticated()){
            return "forgot-password";
        }else if (session.getAttribute("otpChecked") != null && (boolean) session.getAttribute("otpChecked") || !authentication.isAuthenticated()) {

            session.removeAttribute("otpChecked");
            session.invalidate();
            return "forgot-password";
        }
        return "redirect:/login";
    }

    @GetMapping("/otp-authentication-form")
    public String showOTPForm(HttpSession session) {
        if (session.getAttribute("emailSearched") != null && (boolean) session.getAttribute("emailSearched")) {

            session.removeAttribute("emailSearched");
            session.invalidate();
            return "otp-authentication";
        }
        return "redirect:/login";
    }

    @GetMapping("/email-check-form")
    public String showEmailCheckForm() {
        return "email-check-for-otp";
    }

    @GetMapping("/admin-password-change-user")
    public String adminPasswordChangeUser() {
        return "admin-password-change-for-user";
    }
}
