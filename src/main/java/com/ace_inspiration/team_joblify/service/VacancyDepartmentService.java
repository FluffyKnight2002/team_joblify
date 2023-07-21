package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.VacancyDepartment;

import java.util.List;

public interface VacancyDepartmentService {
    VacancyDepartment createdVacancyDepartments(VacancyDto vacancyDto);
    List<VacancyDepartment> selectAllVacancyDepartments();
    VacancyDepartment updateVacancyDepartments(VacancyDto vacancyDto);
    void closedVacancyDepartments(long id);
}
