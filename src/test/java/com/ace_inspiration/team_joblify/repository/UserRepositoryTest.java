package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Department;
import com.ace_inspiration.team_joblify.entity.Gender;
import com.ace_inspiration.team_joblify.entity.Role;
import com.ace_inspiration.team_joblify.entity.User;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private ResourceLoader resourceLoader;

    User user = null;
    Department department = null;

    @BeforeEach
    void setUp() throws IOException {

        LocalDateTime now = LocalDateTime.now();

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


        User users = User.builder()
                .username("User")
                .name("User")
                .phone("097776788467")
                .email("aungkhetnzaw@gmail.com")
                .address("Ace Data System")
                .photo(Base64.encodeBase64String(photoBytes))
                .gender(Gender.FEMALE)
                .password("1122121")
                .role(Role.DEFAULT_HR)
                .note("This is User Account")
                .department(department)
                .createdDate(now)
                .lastUpdatedDate(now)
                .build();
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Admin", "AKZ"})
    void findByUsername(String username) {
        Optional<User> u = userRepository.findByUsername(username);
        if(u.isPresent()){
            assertThat(u).contains(user);
        } else {
            assertThat(u).isEmpty();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"ace@gmail.com", "ak4312040@gmail.com"})
    void findByEmail(String email) {
        Optional<User> u = userRepository.findByEmail(email);
        if(u.isPresent()){
            assertThat(u).contains(user);
        } else {
            assertThat(u).isEmpty();
        }
    }

    @ParameterizedTest
    @EnumSource(Role.class)
    void findByRole(Role role) {

        Optional<User> u = userRepository.findByRole(role);
        if(u.isPresent()){
            assertThat(u).contains(user);
        } else {
            assertThat(u).isEmpty();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"09777159555", "09773137253"})
    void findByPhone(String phone) {

        Optional<User> u = userRepository.findByPhone(phone);
        if(!u.isEmpty()){
            assertThat(u).contains(user);
        } else {
            assertThat(u).isEmpty();
        }
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2})
    void findByUsernameExceptHimself(long userId) {
        Optional<User> u = userRepository.findByUsernameAndIdNot("Admin", userId);
        if(u.isPresent()){
            assertThat(u).contains(user);
        } else {
            assertThat(u).isEmpty();
        }
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2})
    void findByEmailExceptHimself(long userId) {
        Optional<User> u = userRepository.findByEmailAndIdNot("ace@gmail.com", userId);
        if(u.isPresent()){
            assertThat(u).contains(user);
        } else {
            assertThat(u).isEmpty();
        }
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2})
    void findByPhoneExceptHimself(long userId) {

        Optional<User> u = userRepository.findByPhoneAndIdNot("09777159555", userId);
        if(!u.isEmpty()){
            assertThat(u).contains(user);
        } else {
            assertThat(u).isEmpty();
        }
    }
}