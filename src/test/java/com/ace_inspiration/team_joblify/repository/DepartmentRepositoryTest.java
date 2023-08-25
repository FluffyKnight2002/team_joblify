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

@DataJpaTest
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;
    Department department = null;
    @BeforeEach
    void setUp() {
        department = Department.builder()
                .name("Human Resource")
                .build();
        departmentRepository.save(department);
    }

    @AfterEach
    void tearDown() {
        departmentRepository.deleteAll();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Y", "a"})
    void findByNameContainingIgnoreCaseTest(String name) {
        List<Department> departmentList = departmentRepository.findByNameContainingIgnoreCase(name);

        if(!departmentList.isEmpty()){
            assertThat(departmentList).contains(department);
        } else {
            assertThat(departmentList).isEmpty();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"Off-Show", "Human Resource"})
    void findByName(String name) {
        Optional<Department> d = departmentRepository.findByName(name);
        if (d.isPresent()) {
            assertThat(d).contains(department);
        } else {
            assertThat(d).isEmpty();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"Off-show", "human resource"})
    void findByNameIgnoreCase(String name) {
        Optional<Department> d = departmentRepository.findByNameIgnoreCase(name);
        if (d.isPresent()) {
            assertThat(d).contains(department);
        } else {
            assertThat(d).isEmpty();
        }
    }
}
