package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Notification;
import com.ace_inspiration.team_joblify.entity.NotificationUser;
import com.ace_inspiration.team_joblify.entity.User;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationUserRepository extends DataTablesRepository<NotificationUser,Long> {

    void deleteAllByUser(User user);
    Optional<NotificationUser> findNotificationUserByNotificationAndUser(Notification notification, User user);
    Optional<NotificationUser> findByNotification(Notification notification);
    List<NotificationUser> findAllByOrderByNotificationDesc();
    List<NotificationUser> findNotificationUserByUser(User user);
}
