package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Notification;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

public interface NotificationRepository extends DataTablesRepository<Notification,Long> {
}
