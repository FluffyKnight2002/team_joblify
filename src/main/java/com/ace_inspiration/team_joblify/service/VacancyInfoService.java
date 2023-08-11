package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.VacancyInfo;
import com.ace_inspiration.team_joblify.entity.VacancyView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.List;

public interface VacancyInfoService {
    VacancyInfo createdVacancyInfo(VacancyDto vacancyDto);
    VacancyInfo reopenVacancyInfo(VacancyDto vacancyDto);
    List<VacancyDto> selectAllVacancyInfo();
    List<VacancyDto> selectLastVacancies();
    DataTablesOutput<VacancyView> getVacanciesDataTable(DataTablesInput input);
    VacancyDto selectVacancyById(long id);
    VacancyInfo updateVacancyInfo(VacancyDto vacancyDto);
    void closedVacancyDepartments(long id);
    Page<VacancyDto> getPaginatedVacancies(Pageable pageable);
    void updateStatusAfter30Days();
    VacancyInfo closeVacancyById(long id);
}
