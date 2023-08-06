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
    private final DepartmentRepository departmentRepository;
    private final PositionService positionService;
    private final AddressRepository addressRepository;
    private final AddressService addressService;
    private final UserRepository userRepository;

    @Override
    public Vacancy createVacancy(VacancyDto vacancyDto) {
        Position position = positionService.checkAndSetPosition(vacancyDto.getPosition());
        Address address = addressService.checkAndSetAddress(vacancyDto.getAddress());
        Department department = departmentService.checkAndSetDepartment(vacancyDto.getDepartment());
        Vacancy vacancy = Vacancy.builder()
                .position(position)
                .department(department)
                .address(address)
                .createdDate(LocalDateTime.now())
                .createdUser(userRepository.findById(vacancyDto.getCreatedUserId()).get())
                .status(Status.OPEN)
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
        Address address = addressService.checkAndSetAddress(updatedVacancyDto.getAddress());
        Vacancy vacancy = vacancyRepository.findById(updatedVacancyDto.getId()).get();
        vacancy.setPosition(position);
        vacancy.setDepartment(department);
        vacancy.setAddress(address);
        return vacancyRepository.save(vacancy);
    }

    @Override
    public void deleteVacancy(long id) {
        vacancyRepository.deleteById(id);
    }
}
