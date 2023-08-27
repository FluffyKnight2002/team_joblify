package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.TechSkills;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import java.util.Optional;

public interface TechSkillsRepository extends DataTablesRepository<TechSkills, Long> {

    Optional<TechSkills> findByNameIgnoreCase(String name);

}
