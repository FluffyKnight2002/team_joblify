package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.VacancyInfo;

import java.util.List;

public interface VacancyInfoService {
    VacancyInfo createdVacancyDepartments(VacancyDto vacancyDto);
    List<VacancyInfo> selectAllVacancyDepartments();
    VacancyInfo updateVacancyDepartments(VacancyDto vacancyDto);
    void closedVacancyDepartments(long id);
}
