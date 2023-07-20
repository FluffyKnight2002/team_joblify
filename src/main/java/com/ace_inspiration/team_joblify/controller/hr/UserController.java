package com.ace_inspiration.team_joblify.controller.hr;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.ace_inspiration.team_joblify.service.hr_service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {


    @GetMapping("/login")
    public String showLoginForm(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("DEFAULT_HR"))) {
            return "redirect:/dashboard";
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

    // @GetMapping("/error-403")
    // public String error403(){
    //     return "error-403";
    // }

    
}
