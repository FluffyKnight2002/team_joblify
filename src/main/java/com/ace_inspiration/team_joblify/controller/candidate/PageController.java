package com.ace_inspiration.team_joblify.controller.candidate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String showIndex() {
        return "index";
    }

    @GetMapping("/contact-us")
    public String showContactUs() {
        return "contact-us";
    }

    @GetMapping("/job-details")
    public String showJobDetails() {
        return "job-details";
    }

    @GetMapping("/all-jobs")
    public String showAllJobs() {
        return "jobs";
    }

}
