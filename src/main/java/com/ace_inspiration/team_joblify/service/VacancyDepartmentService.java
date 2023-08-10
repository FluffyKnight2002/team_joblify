package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.Vacancyinfo;

import java.util.List;

public interface VacancyDepartmentService {
    Vacancyinfo createdVacancyDepartments(VacancyDto vacancyDto);
    List<Vacancyinfo> selectAllVacancyDepartments();
    Vacancyinfo updateVacancyDepartments(VacancyDto vacancyDto);
    void closedVacancyDepartments(long id);
}
