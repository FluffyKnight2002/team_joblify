package com.ace_inspiration.team_joblify.controller.hr;


import com.ace_inspiration.team_joblify.entity.User;
import com.ace_inspiration.team_joblify.service.hr_service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

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
    public String showUserProfileEdit(@RequestParam("id")long id, Model model){
        User user=userService.findById(id).orElseThrow(()-> new NoSuchElementException("User Not Found"));
        model.addAttribute("user", user);
        return "user-profile-edit";
    }

     @GetMapping("/user-password-edit")
     public String showUserPasswordEditForm(){
         return "user-password-edit-form";
     }



    
}
