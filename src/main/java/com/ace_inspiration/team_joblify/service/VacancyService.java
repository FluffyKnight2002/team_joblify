package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.dto.VacancyDto;

import java.util.List;

public interface VacancyService {

    void createVacancy(VacancyDto vacancyDto);
    List<VacancyDto> selectAllVacancy();
    void updateVacancy(VacancyDto vacancyDto);
    void deleteVacancy(long id);

}
