package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.*;
import com.ace_inspiration.team_joblify.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class VacancyInfoServiceImpl implements VacancyInfoService {

    private final VacancyInfoRepository vacancyInfoRepository;
    private final VacancyViewRepository vacancyViewRepository;
    private final VacancyService vacancyService;
    private final VacancyRepository vacancyRepository;
    private final PositionRepository positionRepository;
    private final PositionService positionService;
    private final DepartmentRepository departmentRepository;
    private final DepartmentService departmentService;
    private final AddressRepository addressRepository;
    private final AddressService addressService;
    private final UserRepository userRepository;

    @Override
    public VacancyInfo createdVacancyInfo(VacancyDto vacancyDto) {
        VacancyInfo vacancyInfo = dtoToEntity(vacancyDto);
        return vacancyInfoRepository.save(vacancyInfo);
    }

    @Override
    public VacancyInfo reopenVacancyInfo(VacancyDto vacancyDto) {
        Address address = addressService.checkAndSetAddress(vacancyDto.getAddress());
        // Update the properties of the existingVacancy based on vacancyDto
        VacancyInfo vacancyInfo = VacancyInfo.builder()
                .vacancy(vacancyService.updateVacancy(vacancyDto))
                .description(vacancyDto.getDescriptions())
                .responsibilities(vacancyDto.getResponsibilities())
                .requirements(vacancyDto.getRequirements())
                .preferences(vacancyDto.getPreferences())
                .onSiteOrRemote(convertOnSiteOrRemote(vacancyDto.getOnSiteOrRemote()))
                .hiredPost(0)
                .workingHours(vacancyDto.getWorkingHours())
                .workingDays(vacancyDto.getWorkingDays())
                .jobType(convertJobType(vacancyDto.getType()))
                .lvl(convertLevel(vacancyDto.getLvl()))
                .post(vacancyDto.getPost())
                .salary(vacancyDto.getSalary())
                .address(address)
                .updatedUser(userRepository.findById(vacancyDto.getUpdatedUserId()).get())
                .updatedTime(LocalDateTime.now())
                .openDate(vacancyDto.getOpenDate())
                .closeDate(vacancyDto.getCloseDate())
                .note(vacancyDto.getNote())
                .status(Status.OPEN)
                .build();
        System.out.println("Vacancy Status : " + vacancyInfo.getStatus());
        return vacancyInfoRepository.save(vacancyInfo);
    }

    @Override
    public List<VacancyDto> selectAllVacancyInfo() {
        List<VacancyDto> vacancyDtos = new ArrayList<>();
        List<VacancyInfo> vacancyInfos = vacancyInfoRepository.findAll();
        for(VacancyInfo vacancyInfo : vacancyInfos) {
            VacancyDto vacancyDto = entityToDto(vacancyInfo);
            vacancyDtos.add(vacancyDto);
        }
        return vacancyDtos;
    }

    @Override
    public List<VacancyDto> selectLastVacancies() {
        List<VacancyInfo> lastVacancies = vacancyInfoRepository.getLastVacancyDepartments();
        List<VacancyDto> vacancyDtos = new ArrayList<>();
        for(VacancyInfo vacancyInfo : lastVacancies) {
            VacancyDto vacancyDto = entityToDto(vacancyInfo);
            vacancyDtos.add(vacancyDto);
        }
        return vacancyDtos;
    }

    @Override
    public DataTablesOutput<VacancyView> getVacanciesDataTable(DataTablesInput input) {
        return vacancyViewRepository.findAll(input);
    }

    @Override
    public VacancyDto selectVacancyById(long id) {
        VacancyDto vacancyDto = new VacancyDto();
        Optional<VacancyInfo> optionalVacancyInfo = vacancyInfoRepository.findById(id);
        System.out.println("Vacancy : ID : " + optionalVacancyInfo.get().getVacancy().getId());
        if(optionalVacancyInfo.isPresent()) {
            vacancyDto = entityToDto(optionalVacancyInfo.get());
        }

        return vacancyDto;
    }

    @Override
    public VacancyInfo updateVacancyInfo(VacancyDto vacancyDto) {
        Long vacancyId = vacancyDto.getId();
        VacancyInfo existingVacancy = null;
        Address address = addressService.checkAndSetAddress(vacancyDto.getAddress());
        existingVacancy = vacancyInfoRepository.findById(vacancyId).orElse(null);
        // Update the properties of the existingVacancy based on vacancyDto
        existingVacancy.setVacancy(vacancyService.updateVacancy(vacancyDto));
        existingVacancy.setDescription(vacancyDto.getDescriptions());
        existingVacancy.setRequirements(vacancyDto.getRequirements());
        existingVacancy.setResponsibilities(vacancyDto.getResponsibilities());
        existingVacancy.setPreferences(vacancyDto.getPreferences());
        existingVacancy.setAddress(address);
        existingVacancy.setWorkingDays(vacancyDto.getWorkingDays());
        existingVacancy.setWorkingHours(vacancyDto.getWorkingHours());
        existingVacancy.setSalary(vacancyDto.getSalary());
        existingVacancy.setPost(vacancyDto.getPost());
        existingVacancy.setJobType(convertJobType(vacancyDto.getType()));
        existingVacancy.setUpdatedUser(userRepository.findById(vacancyDto.getUpdatedUserId()).get());
        existingVacancy.setUpdatedTime(LocalDateTime.now());
        existingVacancy.setLvl(convertLevel(vacancyDto.getLvl()));
        existingVacancy.setOnSiteOrRemote(convertOnSiteOrRemote(vacancyDto.getOnSiteOrRemote()));
        existingVacancy.setNote(vacancyDto.getNote());

        // Save the updated or new entity
        return vacancyInfoRepository.save(existingVacancy);
    }

    @Override
    public void closedVacancyDepartments(long id) {

    }

    @Override
    public Page<VacancyDto> getPaginatedVacancies(Pageable pageable) {
        Page<VacancyInfo> vacanciesPage = vacancyInfoRepository.findAll(pageable);
        List<VacancyDto> vacancyDtos = vacanciesPage.getContent().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(vacancyDtos, pageable, vacanciesPage.getTotalElements());
    }

    // The scheduled task will be executed every day at 12:00 PM (noon)
    @Override
    @Scheduled(cron = "0 0 12 * * ?")
    public void updateStatusAfter30Days() {
        List<VacancyInfo> vacancyInfos = vacancyInfoRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        for (VacancyInfo vacancyInfo : vacancyInfos) {
            LocalDate openDate = vacancyInfo.getOpenDate();
            long daysDifference = ChronoUnit.DAYS.between(openDate, currentDate);

            if (daysDifference >= 30) {
                vacancyInfo.setStatus(Status.CLOSED);
                vacancyInfoRepository.save(vacancyInfo);
            }
        }
    }

    @Override
    public VacancyInfo closeVacancyById(long id) {
        Optional<VacancyInfo> optionalVacancyInfo = vacancyInfoRepository.findById(id);
        VacancyInfo vacancyInfo = null;
        if(optionalVacancyInfo.isEmpty()){
            return vacancyInfo;
        }
        vacancyInfo = optionalVacancyInfo.get();
        vacancyInfo.setStatus(Status.CLOSED);
        return vacancyInfoRepository.save(vacancyInfo);
    }

    private VacancyInfo dtoToEntity(VacancyDto vacancyDto) {
        Address address = addressService.checkAndSetAddress(vacancyDto.getAddress());
        VacancyInfo vacancyInfo = VacancyInfo.builder()
                .vacancy(vacancyService.createVacancy(vacancyDto))
                .description(vacancyDto.getDescriptions())
                .responsibilities(vacancyDto.getResponsibilities())
                .requirements(vacancyDto.getRequirements())
                .preferences(vacancyDto.getPreferences())
                .onSiteOrRemote(convertOnSiteOrRemote(vacancyDto.getOnSiteOrRemote()))
                .address(address)
                .hiredPost(0)
                .workingHours(vacancyDto.getWorkingHours())
                .workingDays(vacancyDto.getWorkingDays())
                .jobType(convertJobType(vacancyDto.getType()))
                .lvl(convertLevel(vacancyDto.getLvl()))
                .post(vacancyDto.getPost())
                .salary(vacancyDto.getSalary())
                .updatedUser(userRepository.findById(vacancyDto.getUpdatedUserId()).get())
                .updatedTime(LocalDateTime.now())
                .openDate(vacancyDto.getOpenDate())
                .closeDate(vacancyDto.getCloseDate())
                .note(vacancyDto.getNote())
                .status(Status.valueOf(vacancyDto.getStatus()))
                .build();
        return vacancyInfo;
    }

    public VacancyDto entityToDto(VacancyInfo vacancyInfo) {
        System.out.println("updated vacancyInfo : " + vacancyInfo.getVacancy().getId());
        VacancyDto vacancyDto = new VacancyDto();
        vacancyDto.setId(vacancyInfo.getId());
        vacancyDto.setVacancyId(vacancyRepository.findById(vacancyInfo.getVacancy().getId()).get().getId());
        vacancyDto.setPosition(vacancyInfo.getVacancy().getPosition().getName());
        vacancyDto.setDepartment(vacancyInfo.getVacancy().getDepartment().getName());
        vacancyDto.setAddress(vacancyInfo.getAddress().getName());
        vacancyDto.setDescriptions(vacancyInfo.getDescription());
        vacancyDto.setResponsibilities(vacancyInfo.getResponsibilities());
        vacancyDto.setRequirements(vacancyInfo.getRequirements());
        vacancyDto.setPreferences(vacancyInfo.getPreferences());
        vacancyDto.setOnSiteOrRemote(String.valueOf(vacancyInfo.getOnSiteOrRemote()));
        vacancyDto.setWorkingHours(vacancyInfo.getWorkingHours());
        vacancyDto.setWorkingDays(vacancyInfo.getWorkingDays());
        vacancyDto.setType(String.valueOf(vacancyInfo.getJobType()));
        vacancyDto.setLvl(String.valueOf(vacancyInfo.getLvl()));
        vacancyDto.setPost(vacancyInfo.getPost());
        vacancyDto.setSalary(vacancyInfo.getSalary());
        String note = (vacancyInfo.getNote() == null) ? "There is no note." : vacancyInfo.getNote();
        vacancyDto.setNote(note);

        vacancyDto.setCreatedUsername(vacancyInfo.getVacancy().getCreatedUser().getUsername());
        vacancyDto.setCreatedDateTime(vacancyInfo.getVacancy().getCreatedUser().getCreatedDate());
        vacancyDto.setUpdatedUsername(vacancyInfo.getUpdatedUser().getUsername());
        vacancyDto.setUpdatedTime(vacancyInfo.getUpdatedTime());
        vacancyDto.setOpenDate(vacancyInfo.getOpenDate());
        vacancyDto.setCloseDate(vacancyInfo.getCloseDate());
        vacancyDto.setStatus(String.valueOf(vacancyInfo.getStatus()));
        return vacancyDto;
    }

    public VacancyDto tupleToVacancyDto(Object[] tuple) {
        VacancyInfo vacancyInfo = (VacancyInfo) tuple[0];
        Long applicantsCount = (Long) tuple[1];

        VacancyDto vacancyDto = entityToDto(vacancyInfo);
        vacancyDto.setApplicants(Integer.valueOf(Math.toIntExact(applicantsCount)));

        return vacancyDto;
    }

    private Level convertLevel(String levelName) {
        return Level.valueOf(levelName);
    }

    private JobType convertJobType(String jobTypeName){
        return JobType.valueOf(jobTypeName);
    }

    private OnSiteOrRemote convertOnSiteOrRemote(String onSiteOrRemote) {
        return OnSiteOrRemote.valueOf(onSiteOrRemote);
    }

}
