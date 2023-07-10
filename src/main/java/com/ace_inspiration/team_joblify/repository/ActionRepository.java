package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Action;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

public interface ActionRepository extends DataTablesRepository<Action,Long> {
}
