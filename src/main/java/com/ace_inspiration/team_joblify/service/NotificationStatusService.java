package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.dto.NotificationDto;
import java.util.List;

public interface NotificationStatusService {

    void createNotification(NotificationDto notificationDto);
    List<NotificationDto> selectAllNotificationStatus();
    void deleteNotificationById(long id);

    long setRead(long notificationId, long userId);

}
