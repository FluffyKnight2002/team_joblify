package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Notification;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends DataTablesRepository<Notification,Long> {
}
