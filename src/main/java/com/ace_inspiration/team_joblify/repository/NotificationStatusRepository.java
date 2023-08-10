package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.NotificationStatus;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

public interface NotificationStatusRepository extends DataTablesRepository<NotificationStatus, Long> {
}
