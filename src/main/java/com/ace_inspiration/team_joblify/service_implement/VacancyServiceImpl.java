package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.AddressRepository;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import com.ace_inspiration.team_joblify.repository.VacancyRepository;
import com.ace_inspiration.team_joblify.service.AddressService;
import com.ace_inspiration.team_joblify.service.DepartmentService;
import com.ace_inspiration.team_joblify.service.PositionService;
import com.ace_inspiration.team_joblify.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {

    private final VacancyRepository vacancyRepository;
    private final DepartmentService departmentService;
    private final PositionService positionService;
    private final UserRepository userRepository;

    @Override
    public Vacancy createVacancy(VacancyDto vacancyDto) {
        Position position = positionService.checkAndSetPosition(vacancyDto.getPosition());
        Department department = departmentService.checkAndSetDepartment(vacancyDto.getDepartment());
        Vacancy vacancy = Vacancy.builder()
                .position(position)
                .department(department)
                .createdDate(LocalDateTime.now())
                .createdUser(userRepository.findById(vacancyDto.getCreatedUserId()).get())
                .build();

        return vacancyRepository.save(vacancy);
    }

    @Override
    public List<VacancyDto> selectAllVacancy() {
        List<Vacancy> lists = vacancyRepository.findAll();
        List<VacancyDto> vacancies = new ArrayList<>();
//        Iterator<Vacancy> itr = lists.iterator();
//        while (itr.hasNext()){
//            Vacancy vacancy = itr.next();
//            VacancyDto dto = VacancyDto.builder()
//                    .id(vacancy.getId())
//                    .position(vacancy.getPosition().getName())
//                    .closeDate(vacancy.getCreatedDate().toLocalDate())
//                    .creadedUsername(vacancy.getCreatedUser())
//                    .address(vacancy.getAddress().getName())
//                    .department(vacancy.getDepartment().getName())
//                    .status(vacancy.getStatus())
//                    .build();
//            vacancies.add(dto);
//        }
        return vacancies;
    }

    @Override
    public Vacancy updateVacancy(VacancyDto updatedVacancyDto) {
        Position position = positionService.checkAndSetPosition(updatedVacancyDto.getPosition());
        Department department = departmentService.checkAndSetDepartment(updatedVacancyDto.getDepartment());
        System.out.println("VacancyId : " + updatedVacancyDto.getVacancyId());
        Vacancy vacancy = vacancyRepository.findById(updatedVacancyDto.getVacancyId()).get();
        vacancy.setPosition(position);
        vacancy.setDepartment(department);
        return vacancyRepository.save(vacancy);
    }

    @Override
    public void deleteVacancy(long id) {
        vacancyRepository.deleteById(id);
    }
}
