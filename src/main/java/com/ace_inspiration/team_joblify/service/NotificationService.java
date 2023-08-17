package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.dto.NotificationDto;

import java.util.List;

public interface NotificationService {
    void createNotifications(NotificationDto notificationDto);
    List<NotificationDto> showNotifications();
    long getNotificationCount();
    void removeNotification(long id);
    void updateNotification(long id);
    void findDeleteAllNotificationUserByUserId(Long userId);
    boolean findNotificationSeenByIdAndUserId(Long notificationId, Long userId);
    void findNotificationByIdAndUserIdAndDelete(Long notificationId, Long userId);
    void findAllNotificationUserByUserIdAndUpdate(Long userId);
    void findNotificationByIdAndUserIdAndUpdate(Long notificationId, Long userId);
    void findNotificationByIdAndUserIdAndUpdate(List<Long>notificationIds, Long userId);
}
