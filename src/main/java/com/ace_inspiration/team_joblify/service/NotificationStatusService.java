package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.dto.NotificationDto;
import com.ace_inspiration.team_joblify.entity.NotificationStatus;

import java.util.List;

public interface NotificationStatusService {

    NotificationStatus createNotification(NotificationStatus notificationStatus);
    List<NotificationDto> selectAllNotificationStatus();
    void deleteNotificationById(long id);

}
