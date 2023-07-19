package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.dto.DepartmentDto;
import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.AddressRepository;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.repository.PositionRepository;
import com.ace_inspiration.team_joblify.repository.VacancyDepartmentRepository;
import com.ace_inspiration.team_joblify.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyDepartmentServiceImpl implements VacancyDepartmentService {

    private final VacancyDepartmentRepository vacancyDepartmentRepository;
    private final VacancyService vacancyService;
    private final PositionRepository positionRepository;
    private final PositionService positionService;
    private final DepartmentRepository departmentRepository;
    private final DepartmentService departmentService;
    private final AddressRepository addressRepository;
    private final AddressService addressService;

    @Override
    public VacancyDepartment createdVacancyDepartments(VacancyDto vacancyDto) {
        Address address = checkAndSetAddress(vacancyDto.getAddress());
        Department department = checkAndSetDepartment(vacancyDto.getDepartment());
        VacancyDepartment vacancyDepartment = VacancyDepartment.builder()
                .vacancy(vacancyService.createVacancy(vacancyDto))
                .address(address)
                .department(department)
                .updatedUser(User.builder().id(1L).build())
                .jobType(vacancyDto.getType())
                .lvl(convertLevel(vacancyDto.getLvl()))
                .post(vacancyDto.getPost())
                .salary(vacancyDto.getSalary())
                .updatedTime(LocalDateTime.now())
                .openDate(LocalDate.now())
                .closeDate(LocalDate.now())
                .note(vacancyDto.getNote())
                .build();
        return vacancyDepartmentRepository.save(vacancyDepartment);
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
        Position position = positionService.findByName(positionName);
        return position;
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
        DepartmentDto departmentDto = DepartmentDto.builder()
                .name(newName)
                .build();
        departmentService.createDepartment(departmentDto);
    }
    private Department convertDepartment(String departmentName) {

        return departmentService.findByName(departmentName);
    }

    private Address checkAndSetAddress(String newAddress) {
        Address address = new Address();
        if(addressRepository.findByName(newAddress) == null) {
            autoFillAddress(newAddress);
        }
        address = convertAddress(newAddress);
        return address;
    }
    private void autoFillAddress(String newName) {
        Address newAddress = Address.builder()
                .name(newName)
                .build();
        addressRepository.save(newAddress);
    }
    private Address convertAddress(String addressName) {
        return addressService.findByName(addressName);
    }

    private Level convertLevel(String levelName) {
        Level level = Level.valueOf(levelName.toUpperCase().replace(" ", "_"));
        return level;
    }

    @Override
    public List<VacancyDepartment> selectAllVacancyDepartments() {
        return null;
    }

    @Override
    public VacancyDepartment updateVacancyDepartments(VacancyDto vacancyDto) {
        return null;
    }

    @Override
    public void closedVacancyDepartments(long id) {

    }
}
