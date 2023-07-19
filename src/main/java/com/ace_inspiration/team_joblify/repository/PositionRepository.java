package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Position;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends DataTablesRepository<Position, Long> {

    List<Position> findByNameContainingIgnoreCase(String term);
    Optional<Position> findByName(String name);

}
