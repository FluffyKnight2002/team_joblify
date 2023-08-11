package com.ace_inspiration.team_joblify.controller.hr;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HRController {

	
	@GetMapping("/view-summary-info")
	public String ViewSummaryinfo() {
		return"view-summary-info";
	}

    @GetMapping("/dashboard")
    public String showDashboard() {
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
