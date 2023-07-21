package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.Vacancy;

import java.util.List;

public interface VacancyService {

    Vacancy createVacancy(VacancyDto vacancyDto);
    List<VacancyDto> selectAllVacancy();
    Vacancy updateVacancy(VacancyDto vacancyDto);
    void deleteVacancy(long id);

}
