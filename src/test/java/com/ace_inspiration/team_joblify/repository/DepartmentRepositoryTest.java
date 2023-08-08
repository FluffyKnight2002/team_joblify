package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Department;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository test;

    private Department department;

    @BeforeEach
    void setUp() {
        Department department = Department.builder()
                .name("Human Resource")
                .build();
        test.save(department);
    }

    @AfterEach
    void tearDown() {
        test.deleteAll();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Y", "a"})
    void findByNameContainingIgnoreCaseTest(String name) {


        List<Department> departmentList = test.findByNameContainingIgnoreCase(name);

        if (!departmentList.isEmpty()) {
            assertThat(departmentList).contains(department);
        } else {
            assertThat(departmentList).isEmpty();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"Off-Show", "Human Resource"})
    void findByName(String name) {

        Optional<Department> d = test.findByName(name);

        if(department.getName().equals(name)){
            assertThat(d).contains(department);
        } else {
            assertThat(d).isEmpty();
        }
    }
}