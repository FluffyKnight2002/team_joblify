package com.ace_inspiration.team_joblify.controller.candidate;

import com.ace_inspiration.team_joblify.service.VacancyDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final VacancyDepartmentService vacancyDepartmentService;

    @GetMapping("/")
    public String showIndex(Model model) {
        model.addAttribute("currentPage", "/");
        model.addAttribute("vacancies", vacancyDepartmentService.selectLastVacancies());
        return "index";
    }

    @GetMapping("/contact-us")
    public String showContactUs(Model model) {
        model.addAttribute("currentPage", "/contact-us");
        return "contact-us";
    }

    @GetMapping("/job-details")
    public String showJobDetails(@RequestParam("id")long id,Model model) {
        model.addAttribute("currentPage", "/all-jobs");
        return "job-details";
    }

    @GetMapping("/all-jobs")
    public String showAllJobs(Model model) {
        model.addAttribute("currentPage", "/all-jobs");
        return "jobs";
    }

}
