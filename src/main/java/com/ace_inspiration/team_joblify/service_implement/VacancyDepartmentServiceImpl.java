package com.ace_inspiration.team_joblify.service_implement;
import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.AddressRepository;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.repository.PositionRepository;
import com.ace_inspiration.team_joblify.repository.VacancyinfoRepository;
import com.ace_inspiration.team_joblify.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyDepartmentServiceImpl implements VacancyDepartmentService {

    private final VacancyinfoRepository vacancyDepartmentRepository;
    private final VacancyService vacancyService;
    private final PositionRepository positionRepository;
    private final PositionService positionService;
    private final DepartmentRepository departmentRepository;
    private final DepartmentService departmentService;
    private final AddressRepository addressRepository;
    private final AddressService addressService;

    @Override
    public VacancyInfo createdVacancyDepartments(VacancyDto vacancyDto) {
        Address address = addressService.checkAndSetAddress(vacancyDto.getAddress());
        Department department = departmentService.checkAndSetDepartment(vacancyDto.getDepartment());
        VacancyInfo vacancyDepartment = VacancyInfo.builder()
                .vacancy(vacancyService.createVacancy(vacancyDto))
                .description(vacancyDto.getDescriptions())
                .responsibilities(vacancyDto.getResponsibilities())
                .requirements(vacancyDto.getRequirements())
                .preferences(vacancyDto.getPreferences())
                .onSiteOrRemote(convertOnSiteOrRemote(vacancyDto.getOnSiteOrRemote()))
                .hiredPost(0)
                .workingHours(vacancyDto.getWorkingHours())
                .workingDays(vacancyDto.getWorkingDays())
                .jobType(JobType.valueOf(vacancyDto.getType()))
                .lvl(convertLevel(vacancyDto.getLvl()))
                .post(vacancyDto.getPost())
                .salary(vacancyDto.getSalary())
                .updatedUser(User.builder().id(1L).build())
                .updatedTime(LocalDateTime.now())
                .openDate(LocalDate.now())
                .closeDate(LocalDate.now())
                .note(vacancyDto.getNote())
                .build();
        return vacancyDepartmentRepository.save(vacancyDepartment);
    }

    private Level convertLevel(String levelName) {
        Level level = Level.valueOf(levelName);
        return level;
    }

    private OnSiteOrRemote convertOnSiteOrRemote(String onSiteOrRemote) {
        OnSiteOrRemote chgOnSiteOrRemote  = OnSiteOrRemote.valueOf(onSiteOrRemote);
        return chgOnSiteOrRemote;
    }

    @Override
    public List<VacancyInfo> selectAllVacancyDepartments() {
        return null;
    }

    @Override
    public VacancyInfo updateVacancyDepartments(VacancyDto vacancyDto) {
        return null;
    }

    @Override
    public void closedVacancyDepartments(long id) {

    }
}
