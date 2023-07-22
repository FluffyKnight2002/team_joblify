package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.dto.NotificationDto;
import com.ace_inspiration.team_joblify.entity.NotificationStatus;

import java.util.List;

public interface NotificationStatusService {

    NotificationStatus createNotification(NotificationDto notificationDto);
    List<NotificationDto> selectAllNotificationStatus();
    void deleteNotificationById(long id);

    long setRead(long notificationId, long userId);

}
