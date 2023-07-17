package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Position;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends DataTablesRepository<Position, Long> {

    @Query(value = "SELECT * FROM position WHERE name LIKE %:term%", nativeQuery = true)
    List<Position> findByNameContainingIgnoreCase(String term);
    @Query(value = "SELECT * FROM position WHERE name = :positionName", nativeQuery = true)
    Position findByName(String positionName);

}
