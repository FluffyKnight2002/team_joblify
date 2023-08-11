package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class VacancyInfoRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private VacancyRepository vacancyRepository;
    @Autowired
    private VacancyInfoRepository vacancyInfoRepository;
    @Autowired
    private AddressRepository addressRepository;

    User user = null;
    Department department = null;
    Vacancy vacancy = null;
    Position position = null;
    VacancyInfo vacancyInfo = null;
    Address address = null;

    @BeforeEach
    void setUp() throws IOException {
        LocalDateTime now = LocalDateTime.now();

        address = Address.builder()
                .name("Ygn")
                .build();
        addressRepository.save(address);

        position = Position.builder()
                .name("Java Developer")
                .build();
        positionRepository.save(position);

        department = Department.builder()
                .name("Human Resources")
                .build();
        departmentRepository.save(department);

        Resource resource = resourceLoader.getResource("classpath:static/assets/images/faces/5.jpg");
        InputStream inputStream = resource.getInputStream();
        byte [] photoBytes = IOUtils.toByteArray(inputStream);

        user = User.builder()
                .username("Admin")
                .name("Admin")
                .phone("09777159555")
                .email("ace@gmail.com")
                .address("Ace Data System")
                .photo(Base64.encodeBase64String(photoBytes))
                .gender(Gender.FEMALE)
                .password("1122121")
                .role(Role.DEFAULT_HR)
                .note("This is Default HR Account")
                .department(department)
                .createdDate(now)
                .lastUpdatedDate(now)
                .build();
        userRepository.save(user);

        vacancy = Vacancy.builder()
                .createdUser(user)
                .position(position)
                .createdDate(now)
                .department(department)
                .build();
        vacancyRepository.save(vacancy);

        vacancyInfo = VacancyInfo.builder()
                .vacancy(vacancy)
                .jobType(JobType.FULL_TIME)
                .lvl(Level.ENTRY_LEVEL)
                .closeDate(LocalDate.now().plusMonths(3))
                .description("aaaa")
                .onSiteOrRemote(OnSiteOrRemote.ON_SITE)
                .post(23)
                .openDate(LocalDate.now())
                .preferences("aaaaaaa")
                .requirements("bbbbb")
                .salary(10000000.5)
                .status(Status.OPEN)
                .updatedTime(now)
                .updatedUser(user)
                .hiredPost(0)
                .workingDays("Mon to Fri")
                .workingDays("9am to 5pm")
                .responsibilities("asdaassfa")
                .address(address)
                .requirements("aaaaaaaa")
                .build();
        vacancyInfoRepository.save(vacancyInfo);
    }

    @AfterEach
    void tearDown() {
        addressRepository.deleteAll();
        positionRepository.deleteAll();
        departmentRepository.deleteAll();
        userRepository.deleteAll();
        vacancyRepository.deleteAll();
        vacancyInfoRepository.deleteAll();
    }

    @Test
    void getLastVacancyInfoTest() {
        List<VacancyInfo> v =  vacancyInfoRepository.getLastVacancyInfo();
        if(!v.isEmpty()) {
            assertThat(v).contains(vacancyInfo);
        } else  {
            assertThat(v).isEmpty();
        }
    }

    @Test
    void findAllByOrderByIdDescTest() {
        List<VacancyInfo> v =  vacancyInfoRepository.findAllByOrderByIdDesc();
        if(!v.isEmpty()) {
            assertThat(v).contains(vacancyInfo);
        } else  {
            assertThat(v).isEmpty();
        }
    }
}