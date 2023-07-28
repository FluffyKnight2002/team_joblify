package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.dto.NotificationDto;

import java.util.List;

public interface NotificationService {
    void createNotifications(NotificationDto notificationDto);
    List<NotificationDto> showNotifications();
    long getNotificationCount();
    void removeNotification(long id);
    void findDeleteAllNotificationUserByUserId(Long userId);
    void findNotificationByIdAndUserIdAndDelete(Long notificationId, Long userId);
}
