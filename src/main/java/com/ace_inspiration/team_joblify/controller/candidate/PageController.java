package com.ace_inspiration.team_joblify.controller.candidate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
	

    @GetMapping("/AA")
    public String showIndex() {
        return "index";
    }

    @GetMapping("/contact-usAA")
    public String showContactUs() {
        return "contact-us";
    }


    @GetMapping("/job-detailAA")
    public String showJobDetails() {
        return "job-details";
    }

    @GetMapping("/all-jobsAA")
    public String showAllJobs() {
        return "jobs";
    }

}
