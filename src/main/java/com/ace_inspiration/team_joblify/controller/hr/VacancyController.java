package com.ace_inspiration.team_joblify.controller.hr;

import com.ace_inspiration.team_joblify.config.MyUserDetails;
import com.ace_inspiration.team_joblify.dto.NotificationDto;
import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.User;
import com.ace_inspiration.team_joblify.entity.VacancyDepartment;
import com.ace_inspiration.team_joblify.service.NotificationStatusService;
import com.ace_inspiration.team_joblify.service.VacancyDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class VacancyController {

    private final VacancyDepartmentService vacancyDepartmentService;
    private final NotificationStatusService notificationStatusService;

    @GetMapping("/show-upload-vacancy-form")
    public String showUploadVacancyForm(){
        return "upload-vacancy";
    }

    @PostMapping("/upload-vacancy")
    public String postVacancy(@ModelAttribute("vacancy")VacancyDto vacancyDto, Authentication authentication) {
        VacancyDepartment vacancyDepartment = vacancyDepartmentService.createdVacancyDepartments(vacancyDto);
        if(vacancyDepartment != null) {
            MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
            NotificationDto notificationDto = new NotificationDto();
            String message = authentication.getName() + " create a " + vacancyDepartment.getVacancy().getPosition().getName() + " vacancy.";
            String link = "/view-vacancy-details/" + vacancyDepartment.getId();
            notificationDto.setMessage(message);
            notificationDto.setLink(link);
            notificationDto.setUser(User.builder().id(myUserDetails.getUserId()).build());
            notificationStatusService.createNotification(notificationDto);
        }
        return "redirect:/show-upload-vacancy-form";
    }

}
