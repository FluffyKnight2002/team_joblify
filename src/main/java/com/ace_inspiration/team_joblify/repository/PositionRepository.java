package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Position;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends DataTablesRepository<Position, Long> {
}
