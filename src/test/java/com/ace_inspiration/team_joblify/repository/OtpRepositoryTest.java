package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OtpRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResourceLoader resourceLoader;

 User user = null;
 Otp otp = null;
    @BeforeEach
    void setUp() throws IOException {

        LocalDateTime now = LocalDateTime.now();

        Department department = Department.builder()
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

        otp = Otp.builder()
                .code("11AA22")
                .user(user)
                .expiredDate(LocalDateTime.now().plusSeconds(10))
                .build();
        otpRepository.save(otp);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        otpRepository.deleteAll();
    }

    @ParameterizedTest
    @ValueSource(strings = {"11AA22", "234SAC"})
    void findByCodeTest(String code) {
        Optional<Otp> o = otpRepository.findByCode(code);
        if(o.isPresent()) {
                assertThat(o).contains(otp);
        } else {
            assertThat(o).isEmpty();
        }
    }

    @Test
    void deleteByExpiredDateLessThanTest() {
        LocalDateTime now = LocalDateTime.now();
        otpRepository.deleteByExpiredDateLessThan(now);
        Optional<Otp> o = otpRepository.findByCode("11AA22");
        if(o.isPresent()) {
            assertThat(o).contains(otp);
        } else {
            assertThat(o).isEmpty();
        }
    }
}