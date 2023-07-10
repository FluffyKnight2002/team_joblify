package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.TechSkills;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

public interface TechSkillsRepository extends DataTablesRepository<TechSkills, Long> {
}
