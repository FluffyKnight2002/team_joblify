package com.ace_inspiration.team_joblify.controller.candidate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.ace_inspiration.team_joblify.dto.CandidateDto;
import com.ace_inspiration.team_joblify.dto.SummaryDto;

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

    @GetMapping("/allJobs")
    public String showAllJobs() {
        return "jobs";
    }

}
