package com.ace_inspiration.team_joblify.controller.hr;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HRController {

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "dashboard";
    }

}
