package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.AddressRepository;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.repository.PositionRepository;
import com.ace_inspiration.team_joblify.repository.VacancyRepository;
import com.ace_inspiration.team_joblify.service.AddressService;
import com.ace_inspiration.team_joblify.service.DepartmentService;
import com.ace_inspiration.team_joblify.service.PositionService;
import com.ace_inspiration.team_joblify.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {

    private final VacancyRepository vacancyRepository;
    private final DepartmentService departmentService;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final PositionService positionService;
    private final AddressRepository addressRepository;
    private final AddressService addressService;

    @Override
    public Vacancy createVacancy(VacancyDto vacancyDto) {
        Position position = checkAndSetPosition(vacancyDto.getPosition());
        Address address = checkAndSetAddress(vacancyDto.getAddress());
        Department department = checkAndSetDepartment(vacancyDto.getDepartment());
        Vacancy vacancy = Vacancy.builder()
                .position(convertPosition(vacancyDto.getPosition()))
                .createdDate(LocalDateTime.now())
                .createdUser(vacancyDto.getCreadedUser())
                .address(address)
                .department(department)
                .status(Status.OPEN)
                .build();

        return vacancyRepository.save(vacancy);
    }

    private Position checkAndSetPosition(String positionName) {
        Position position = new Position();
        if(positionRepository.findByName(positionName) == null) {
            autoFillPosition(positionName);
        }
        position = convertPosition(positionName);
        return position;
    }
    private void autoFillPosition(String newName) {
        Position newPosition = Position.builder()
                .name(newName)
                .build();
        positionService.addPosition(newPosition);
    }
    private Position convertPosition(String positionName) {
        return positionService.findByName(positionName);
    }

    private Department checkAndSetDepartment(String departmentName) {
        Department department = new Department();
        if(departmentRepository.findByName(departmentName) == null) {
            autoFillDepartment(departmentName);
        }
        department = convertDepartment(departmentName);
        return department;
    }
    private void autoFillDepartment(String newName) {
        Department departmentDto = Department.builder()
                .name(newName)
                .build();
        departmentService.createDepartment(departmentDto);
    }
    private Department convertDepartment(String departmentName) {
        return departmentService.findByName(departmentName);
    }

    private Address checkAndSetAddress(String newAddress) {
        Address address = new Address();
        if(addressService.findByName(newAddress) == null) {
            autoFillAddress(newAddress);
        }
        address = convertAddress(newAddress);
        return address;
    }
    private void autoFillAddress(String newName) {
        Address newAddress = Address.builder()
                .name(newName)
                .build();
        addressService.addAddress(newAddress);
    }
    private Address convertAddress(String addressName) {
        return addressService.findByName(addressName);
    }

    private Level convertLevel(String levelName) {
        Level level = Level.valueOf(levelName.toUpperCase().replace(" ", "_"));
        return level;
    }

    @Override
    public List<VacancyDto> selectAllVacancy() {
        List<Vacancy> lists = vacancyRepository.findAll();
        List<VacancyDto> vacancies = new ArrayList<>();
        Iterator<Vacancy> itr = lists.iterator();
        while (itr.hasNext()){
            Vacancy vacancy = itr.next();
            VacancyDto dto = VacancyDto.builder()
                    .id(vacancy.getId())
                    .position(vacancy.getPosition().getName())
                    .closeDate(vacancy.getCreatedDate().toLocalDate())
                    .creadedUser(vacancy.getCreatedUser())
                    .address(vacancy.getAddress().getName())
                    .department(vacancy.getDepartment().getName())
                    .status(vacancy.getStatus())
                    .build();
            vacancies.add(dto);
        }
        return vacancies;
    }

    @Override
    public Vacancy updateVacancy(VacancyDto updatedVacancyDto) {
        Position position = checkAndSetPosition(updatedVacancyDto.getPosition());
        Department department = checkAndSetDepartment(updatedVacancyDto.getDepartment());
        Address address = checkAndSetAddress(updatedVacancyDto.getAddress());
        Vacancy vacancy = vacancyRepository.findById(updatedVacancyDto.getId()).get();
        vacancy.setId(updatedVacancyDto.getId());
        vacancy.setPosition(convertPosition(updatedVacancyDto.getPosition()));
        vacancy.setDepartment(department);
        vacancy.setAddress(address);
        vacancy.setStatus(updatedVacancyDto.getStatus());
        return vacancyRepository.save(vacancy);
    }

    @Override
    public void deleteVacancy(long id) {
        vacancyRepository.deleteById(id);
    }
}
