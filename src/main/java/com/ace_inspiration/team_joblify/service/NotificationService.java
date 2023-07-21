package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.dto.NotificationDto;
import com.ace_inspiration.team_joblify.entity.Notification;

import java.util.List;

public interface NotificationService {
    Notification createNotifications(NotificationDto notificationDto);
    List<Notification> showNotifications();
    void removeNotification(long id);
}
