package com.ace_inspiration.team_joblify.controller.hr;

import com.ace_inspiration.team_joblify.config.MyUserDetails;
import com.ace_inspiration.team_joblify.dto.NotificationDto;
import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.JobType;
import com.ace_inspiration.team_joblify.entity.Level;
import com.ace_inspiration.team_joblify.entity.OnSiteOrRemote;
import com.ace_inspiration.team_joblify.entity.VacancyInfo;
import com.ace_inspiration.team_joblify.service.VacancyInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class VacancyController {

    private final VacancyInfoService vacancyInfoService;
    private final NotificationCreator notificationCreator;

    @GetMapping("/show-upload-vacancy-form")
    public String showUploadVacancyForm(){
        return "upload-vacancy";
    }

    @PostMapping("/upload-vacancy")
    @ResponseBody
    public boolean postVacancy(@RequestBody VacancyDto vacancyDto, Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        vacancyDto.setCreatedUserId(myUserDetails.getUserId());
        vacancyDto.setUpdatedUserId(myUserDetails.getUserId());
        // For close date plus 30
        vacancyDto.setOpenDate(LocalDate.now());
        vacancyDto.setCloseDate(vacancyDto.getOpenDate().plusDays(30));
        vacancyDto.setStatus("OPEN");
        VacancyInfo vacancyInfo = vacancyInfoService.createdVacancyInfo(vacancyDto);
        if(vacancyInfo != null) {
            NotificationDto notificationDto = new NotificationDto();
            String message = authentication.getName() + " create a " + vacancyInfo.getVacancy().getPosition().getName() + " vacancy.";
            String link = "/show-all-vacancies-page?id=" + vacancyInfo.getId();
            notificationCreator.createNotification(myUserDetails,message,link);
            return true;
        }
        return false;
    }

    @PostMapping("/reopen-vacancy")
    @ResponseBody
    public boolean reopenVacancy(@RequestBody VacancyDto vacancyDto, Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        vacancyDto.setCreatedUserId(myUserDetails.getUserId());
        vacancyDto.setUpdatedUserId(myUserDetails.getUserId());
        vacancyDto.setStatus("OPEN");
        // For close date plus 30
        vacancyDto.setOpenDate(LocalDate.now());
        vacancyDto.setCloseDate(vacancyDto.getOpenDate().plusDays(30));
        VacancyInfo vacancyInfo = vacancyInfoService.reopenVacancyInfo(vacancyDto);
        if(vacancyInfo != null) {
            NotificationDto notificationDto = new NotificationDto();
            String message = authentication.getName() + " reopen " + vacancyInfo.getVacancy().getPosition().getName() + " vacancy.";
            String link = "/show-all-vacancies-page?id=" + vacancyInfo.getId();
            notificationCreator.createNotification(myUserDetails,message,link);
            return true;
        }
        return false;
    }

    @PostMapping("/reopen-vacancy-by-id")
    @ResponseBody
    public boolean reopenVacancyById(@RequestParam("id")String id, Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        VacancyDto vacancyDto = new VacancyDto();
        vacancyDto.setCreatedUserId(myUserDetails.getUserId());
        vacancyDto.setUpdatedUserId(myUserDetails.getUserId());
        vacancyDto.setStatus("OPEN");
        // For close date plus 30
        vacancyDto.setOpenDate(LocalDate.now());
        vacancyDto.setCloseDate(vacancyDto.getOpenDate().plusDays(30));
        vacancyDto.setId(Long.valueOf(id));
        System.out.println("ID : " + vacancyDto.getId());
        VacancyInfo vacancyInfo = vacancyInfoService.reopenVacancyInfoById(vacancyDto);// By Id mean findBy id and update in service
        if(vacancyInfo != null) {
            NotificationDto notificationDto = new NotificationDto();
            String message = authentication.getName() + " reopen " + vacancyInfo.getVacancy().getPosition().getName() + " vacancy.";
            String link = "/show-all-vacancies-page?id=" + vacancyInfo.getId();
            notificationCreator.createNotification(myUserDetails,message,link);
            return true;
        }
        return false;
    }

    @GetMapping("/show-all-vacancies-page")
    public String showAllVacancyPage(Model model) {
        model.addAttribute("currentPage" , "/show-all-vacancies-page");
        return "all-vacancies";
    }

    @GetMapping("/view-vacancy-detail")
    public String showAllVacancyPage(@RequestParam("id")long id, Model model) {
        VacancyDto vacancyDto = vacancyInfoService.selectVacancyById(id);
        System.out.println("Vacancy ID : " + vacancyDto.getVacancyId());
        System.out.println(vacancyDto.getPosition());
        System.out.println(vacancyDto.getDescriptions());
        System.out.println(vacancyDto.getLvl());
        model.addAttribute("currentPage" , "/show-all-vacancies-page");
        model.addAttribute("vacancy", vacancyInfoService.selectVacancyById(id));
        return "vacancy-details";
    }

    @PostMapping("/update-vacancy")
    @ResponseBody
    public boolean updateVacancy(@RequestBody VacancyDto vacancyDto, Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        vacancyDto.setUpdatedUserId(myUserDetails.getUserId());
        VacancyInfo vacancyInfo = vacancyInfoService.updateVacancyInfo(vacancyDto);
        if(vacancyInfo != null) {
            NotificationDto notificationDto = new NotificationDto();
            String message = authentication.getName() + " update " + vacancyInfo.getVacancy().getPosition().getName() + " vacancy.";
            String link = "/show-all-vacancies-page?id=" + vacancyInfo.getId();
            notificationCreator.createNotification(myUserDetails,message,link);
            return true;
        }
        return false;
    }

    @PostMapping("/close-vacancy")
    @ResponseBody
    public boolean closeVacancy(@RequestParam("id")long id, Authentication authentication) {
        System.out.println("ID : " + id);
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        VacancyInfo vacancyInfo = vacancyInfoService.closeVacancyById(id);
        if(vacancyInfo != null) {
            NotificationDto notificationDto = new NotificationDto();
            String message = authentication.getName() + " close " + vacancyInfo.getVacancy().getPosition().getName() + " vacancy.";
            String link = "/show-all-vacancies-page?id=" + vacancyInfo.getId();
            notificationCreator.createNotification(myUserDetails,message,link);
            return true;
        }
        return false;
    }

    // Modal attributes to show select input
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
