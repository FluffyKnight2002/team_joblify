package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.VacancyInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VacancyDepartmentService {
    VacancyInfo createdVacancyDepartments(VacancyDto vacancyDto);
    List<VacancyDto> selectAllVacancyDepartments();
    List<VacancyDto> selectLastVacancies();
    VacancyDto selectVacancyById(long id);
    VacancyInfo updateVacancyDepartments(VacancyDto vacancyDto);
    void closedVacancyDepartments(long id);
    Page<VacancyDto> getPaginatedVacancies(Pageable pageable);
}
