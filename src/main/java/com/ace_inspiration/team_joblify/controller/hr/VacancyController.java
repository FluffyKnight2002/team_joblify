package com.ace_inspiration.team_joblify.controller.hr;

import com.ace_inspiration.team_joblify.config.MyUserDetails;
import com.ace_inspiration.team_joblify.dto.NotificationDto;
import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.JobType;
import com.ace_inspiration.team_joblify.entity.Level;
import com.ace_inspiration.team_joblify.entity.OnSiteOrRemote;
import com.ace_inspiration.team_joblify.entity.VacancyInfo;
import com.ace_inspiration.team_joblify.service.NotificationService;
import com.ace_inspiration.team_joblify.service.VacancyInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class VacancyController {

    private final VacancyInfoService vacancyInfoService;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/show-upload-vacancy-form")
    public String showUploadVacancyForm(){
        return "upload-vacancy";
    }

    @PostMapping("/upload-vacancy")
    public String postVacancy(@ModelAttribute("vacancy")VacancyDto vacancyDto, Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        vacancyDto.setCreatedUserId(myUserDetails.getUserId());
        vacancyDto.setUpdatedUserId(myUserDetails.getUserId());
        VacancyInfo vacancyInfo = vacancyInfoService.createdVacancyInfo(vacancyDto);
        if(vacancyInfo != null) {
            NotificationDto notificationDto = new NotificationDto();
            String message = authentication.getName() + " create a " + vacancyInfo.getVacancy().getPosition().getName() + " vacancy.";
            String link = "/show-all-vacancies-page?id=" + vacancyInfo.getId();
            notificationDto.setMessage(message);
            notificationDto.setLink(link);
            notificationDto.setUserId(myUserDetails.getUserId());
            notificationDto.setUsername(myUserDetails.getUsername());
            notificationDto.setTime(LocalDateTime.now());
            notificationService.createNotifications(notificationDto);
            messagingTemplate.convertAndSend("/all/notifications", notificationDto);
        }
        return "redirect:/show-upload-vacancy-form";
    }

    @GetMapping("/show-all-vacancies-page")
    public String showAllVacancyPage(Model model) {
        model.addAttribute("currentPage" , "/show-all-vacancies-page");
        return "all-vacancies";
    }

    @GetMapping("/view-vacancy-detail")
    public String showAllVacancyPage(@RequestParam("id")long id, Model model) {
        VacancyDto vacancyDto = vacancyInfoService.selectVacancyById(id);
        System.out.println(vacancyDto.getPosition());
        System.out.println(vacancyDto.getDescriptions());
        System.out.println(vacancyDto.getLvl());
        model.addAttribute("currentPage" , "/show-all-vacancies-page");
        model.addAttribute("vacancy", vacancyInfoService.selectVacancyById(id));
        return "vacancy-details";
    }

    @PostMapping("/update-vacancy")
    public String updateVacancy(@ModelAttribute("vacancy")VacancyDto vacancyDto, Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        vacancyDto.setUpdatedUserId(myUserDetails.getUserId());
        VacancyInfo vacancyInfo = vacancyInfoService.updateVacancyInfo(vacancyDto);
        if(vacancyInfo != null) {
            NotificationDto notificationDto = new NotificationDto();
            String message = authentication.getName() + " update " + vacancyInfo.getVacancy().getPosition().getName() + " vacancy.";
            String link = "/show-all-vacancies-page?id=" + vacancyInfo.getId();
            notificationDto.setMessage(message);
            notificationDto.setLink(link);
            notificationDto.setUserId(myUserDetails.getUserId());
            notificationDto.setUsername(myUserDetails.getUsername());
            notificationDto.setTime(LocalDateTime.now());
            notificationService.createNotifications(notificationDto);
            messagingTemplate.convertAndSend("/all/notifications", notificationDto);
        }
        return "redirect:/show-all-vacancies-page";
    }

    @PostMapping("/close-vacancy")
    @ResponseBody
    public boolean closeVacancy(@RequestParam("id")long id) {
        return vacancyInfoService.closeVacancyById(id);
    }

    @ModelAttribute("experienceLevels")
    public Level[] getExperienceLevel() {
        return Level.values();
    }

    @ModelAttribute("onSiteOrRemoteList")
    public OnSiteOrRemote[] getOnSiteOrRemoteList() {
        return OnSiteOrRemote.values();
    }

    @ModelAttribute("typeList")
    public JobType[] getTypeList() {
        return JobType.values();
    }

}
