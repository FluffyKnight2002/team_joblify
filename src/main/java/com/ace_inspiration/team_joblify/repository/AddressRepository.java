package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Address;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends DataTablesRepository<Address, Long> {
    List<Address> findByNameContainingIgnoreCase(String term);
    Optional<Address> findByName(String name);
}
