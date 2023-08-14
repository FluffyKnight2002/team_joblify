package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PositionRepositoryTest {

    @Autowired
    private PositionRepository positionRepository;

    Position position = null;

    @BeforeEach
    void setUp() {
        position = Position.builder()
                .name("Human Resources")
                .build();
        positionRepository.save(position);
    }

    @AfterEach
    void tearDown() {
        positionRepository.deleteAll();
    }

    @ParameterizedTest
    @ValueSource(strings = {"y", "h"})
    void findByNameContainingIgnoreCase(String name) {
        List<Position> p = positionRepository.findByNameContainingIgnoreCase(name);
        if(!p.isEmpty()) {
            assertThat(p).contains(position);
        } else {
            assertThat(p).isEmpty();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"Off-Show", "Human Resources"})
    void findByName(String name) {
        Optional<Position> p = positionRepository.findByName(name);
        if (p.isPresent()) {
            assertThat(p).contains(position);
        } else {
            assertThat(p).isEmpty();
        }
    }
}