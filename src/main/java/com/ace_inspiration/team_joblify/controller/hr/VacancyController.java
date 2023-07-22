package com.ace_inspiration.team_joblify.controller.hr;

import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.service.NotificationService;
import com.ace_inspiration.team_joblify.service.VacancyDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class VacancyController {

    private final VacancyDepartmentService vacancyDepartmentService;
    private final NotificationService notificationService;

    @GetMapping("/show-upload-vacancy-form")
    public String showUploadVacancyForm(){
        return "upload-vacancy";
    }

    @PostMapping("/upload-vacancy")
        public String postVacancy (VacancyDto vacancyDto){
            vacancyDepartmentService.createdVacancyDepartments(vacancyDto);
            return "redirect:/show-upload-vacancy-form";

    }


}
