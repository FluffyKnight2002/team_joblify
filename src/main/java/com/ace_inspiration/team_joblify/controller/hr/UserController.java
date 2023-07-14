package com.ace_inspiration.team_joblify.controller.hr;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/login")
    public String showLoginForm(){
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

}
