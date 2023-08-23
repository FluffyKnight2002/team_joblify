package com.ace_inspiration.team_joblify.controller.candidate;

import com.ace_inspiration.team_joblify.service.VacancyInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final VacancyInfoService vacancyInfoService;

    @GetMapping("/")
    public String showIndex(Model model) {
        model.addAttribute("currentPage", "/");
        model.addAttribute("vacancies", vacancyInfoService.selectLastVacancies());
        return "index";
    }

    @GetMapping("/contact-us")
    public String showContactUs(Model model) {
        model.addAttribute("currentPage", "/contact-us");
        return "contact-us";
    }

    @GetMapping("/job-detail")
    public String showJobDetails(@RequestParam("id")long id,Model model) {
        model.addAttribute("currentPage", "/all-jobs");
        return "job-details";
    }

    @GetMapping("/all-jobs")
    public String showAllJobs(Model model) {
        model.addAttribute("currentPage", "/all-jobs");
        return "jobs";
    }

    @GetMapping("/400")
    public String badRequest() {
        return "error-400";
    }

    @GetMapping("/401")
    public String unauthorizedAccess() {
        return "error-401";
    }

    @GetMapping("/403")
    public String dontHaveAuthority() {
        return "error-403";
    }

    @GetMapping("/404")
    public String notFound() {
        return "error-404";
    }

    @GetMapping("/405")
    public String methodNotAllowed() {
        return "error-405";
    }

    @GetMapping("/500")
    public String internalServerError() {
        return "error-500";
    }

    @GetMapping("/502")
    public String badGateway() {
        return "error-502";
    }

    @GetMapping("/503")
    public String serviceUnavailiable() {
        return "error-503";
    }

    @GetMapping("/504")
    public String gatewayTimeOut() {
        return "error-504";
    }
}
