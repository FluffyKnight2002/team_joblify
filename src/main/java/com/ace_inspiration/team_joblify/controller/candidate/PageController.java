package com.ace_inspiration.team_joblify.controller.candidate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String showIndex() {
        return "index";
    }

    @GetMapping("/contactUs")
    public String showContactUs() {
        return "contact-us";
    }

    @GetMapping("/jobDetails")
    public String showJobDetails() {
        return "job-details";
    }

    @GetMapping("/allJobs")
    public String showAllJobs() {
        return "jobs";
    }

}
