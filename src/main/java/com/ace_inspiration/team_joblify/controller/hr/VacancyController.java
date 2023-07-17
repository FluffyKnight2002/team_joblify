package com.ace_inspiration.team_joblify.controller.hr;

import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.Level;
import com.ace_inspiration.team_joblify.service.PositionService;
import com.ace_inspiration.team_joblify.service.VacancyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class VacancyController {

    private final VacancyService vacancyService;

    private final PositionService positionService;

    public VacancyController(VacancyService vacancyService, PositionService positionService) {
        this.vacancyService = vacancyService;
        this.positionService = positionService;
    }

    @GetMapping("/show-upload-vacancy-form")
    public String showUploadVacancyForm(){
        return "upload-vacancy";
    }

    @PostMapping("/upload-vacancy")
    public String postVacancy(@ModelAttribute("vacancy")VacancyDto vacancyDto){
        vacancyService.createVacancy(vacancyDto);
        return "redirect:show-upload-vacancy-form";
    }

    @ModelAttribute("vacancy")
    public VacancyDto getVacancyDto() {
        return new VacancyDto();
    }

    @ModelAttribute("lvlList")
    public Level[] getFormattedLevelList() {
        return Level.values();
    }

}
