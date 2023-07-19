package com.ace_inspiration.team_joblify.controller.hr;

import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.Level;
import com.ace_inspiration.team_joblify.service.VacancyDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class VacancyController {

    private final VacancyDepartmentService vacancyDepartmentService;

    @GetMapping("/show-upload-vacancy-form")
    public String showUploadVacancyForm(){
        return "upload-vacancy";
    }

    @PostMapping("/upload-vacancy")
    public String postVacancy(@ModelAttribute("vacancy")VacancyDto vacancyDto) {
        vacancyDepartmentService.createdVacancyDepartments(vacancyDto);
//        if(vacancyDepartment != null) {
//            String notification = "User1 upload " + vacancyDto.getPosition() + " vacancy.";
//            notificationService.createNotifications(notification);
//        }
        return "redirect:show-upload-vacancy-form";
    }

    // ModalAttributes session start
    @ModelAttribute("vacancy")
    public VacancyDto getVacancyDto() {
        return new VacancyDto();
    }

    @ModelAttribute("lvlList")
    public Level[] getFormattedLevelList() {

        return Level.values();
    }

}
