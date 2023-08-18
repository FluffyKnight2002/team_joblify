package com.ace_inspiration.team_joblify.controller.hr;

import com.ace_inspiration.team_joblify.config.MyUserDetails;
import com.ace_inspiration.team_joblify.dto.NotificationDto;
import com.ace_inspiration.team_joblify.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class NotificationCreator {

    public final NotificationService notificationService;
    public final SimpMessagingTemplate messagingTemplate;

    // Create notification
    public void createNotification(MyUserDetails myUserDetails, String message, String link) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setMessage(message);
        notificationDto.setLink(link);
        notificationDto.setUserId(myUserDetails.getUserId());
        notificationDto.setUsername(myUserDetails.getUsername());
        notificationDto.setTime(LocalDateTime.now());
        notificationService.createNotifications(notificationDto);
        messagingTemplate.convertAndSend("/all/notifications", notificationDto);
    }
}
