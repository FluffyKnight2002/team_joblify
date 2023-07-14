package com.ace_inspiration.team_joblify.controller.hr;

import com.ace_inspiration.team_joblify.config.MyUserDetails;
import com.ace_inspiration.team_joblify.dto.UserDto;
import com.ace_inspiration.team_joblify.service.hr_service.HRService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
@AllArgsConstructor
public class HRController {

    private final HRService hrService;
    @GetMapping("/login")
     public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String admin() {
        return "dashboard";
    }

    @GetMapping("/user-register")
    public String userRegister() {
        return "user-register";
    }

    @PostMapping("/user-register")
    public String userRegisterPost(UserDto userDto, Authentication authentication) throws IOException {
        MyUserDetails myUserDetails= (MyUserDetails) authentication.getPrincipal();
        hrService.userCreate(userDto, myUserDetails.getUserId());
        return "redirect:/user-register";
    }

    @GetMapping("/all-user-list")
    public String allUserList() {
        return "all-user-list";
    }


}
