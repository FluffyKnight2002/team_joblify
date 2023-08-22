package com.ace_inspiration.team_joblify.controller.hr;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HRController {

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("currentPage", "/dashboard");
        return "dashboard";
    }

    @GetMapping("/otp")
    public String otp() {
        return "otp";
    }

    @GetMapping("/invite-mail")
    public String inviteMail() {
        return "invite-mail";
    }
}
