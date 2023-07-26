package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Notification;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends DataTablesRepository<Notification,Long>, JpaRepository<Notification,Long> {
    List<Notification> findAllByOrderByTimeDesc();

}
