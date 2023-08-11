package com.ace_inspiration.team_joblify.repository;

import  com.ace_inspiration.team_joblify.entity.Address;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class AddressRepositoryTest {

    @Autowired
    private AddressRepository test;

    private Address address;

    @BeforeEach
    void setUp() {
        address = Address.builder()
                .name("Yangon")
                .build();
        test.save(address);
    }
    @AfterEach
    void tearDown() {
        test.deleteAll();
    }
    @ParameterizedTest
    @ValueSource(strings = {"z", "Y"})
    void findByNameContainingIgnoreCaseTest(String name) {

        List<Address> addressList = test.findByNameContainingIgnoreCase(name);
        if(!addressList.isEmpty()) {
            assertThat(addressList).contains(address);
        } else {
            assertThat(addressList).isEmpty();
        }
    }


    @ParameterizedTest
    @ValueSource(strings = {"Mandalay", "Yangon"})
    void findByNameTest(String name) {

        Optional<Address> foundAddress = test.findByName(name);

        if (foundAddress.isPresent()) {
            assertThat(foundAddress).contains(address);
        } else {
            assertThat(foundAddress).isEmpty();
        }
    }



}