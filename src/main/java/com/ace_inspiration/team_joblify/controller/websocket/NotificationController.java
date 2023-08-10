package com.ace_inspiration.team_joblify.controller.websocket;

import com.ace_inspiration.team_joblify.config.MyUserDetails;
import com.ace_inspiration.team_joblify.dto.NotificationDto;
import com.ace_inspiration.team_joblify.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final List<NotificationDto> notifications = new ArrayList<>();
    private final NotificationService notificationService;

    @GetMapping("/show")
    public List<NotificationDto> getAllNotifications(){
        return notificationService.showNotifications();
    }

    @GetMapping("/count")
    public long getNotificationsCount(){
        return notificationService.getNotificationCount();
    }



    @GetMapping("/makeAsRead")
    public void makeNotificationAsRead(@RequestParam("id")Long notificationId, Authentication authentication){

        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        // Find the notification by both notification_id and user_id
        notificationService.findNotificationByIdAndUserIdAndDelete(notificationId, myUserDetails.getUserId());
    }

    @GetMapping("/makeAllAsRead")
    public void makeAllNotificationAsRead(Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        notificationService.findDeleteAllNotificationUserByUserId(myUserDetails.getUserId());
    }

    @GetMapping("/delete")
    public void makeAllNotificationAsRead(@RequestParam("id")Long notificationId) {
        notificationService.removeNotification(notificationId);
    }

}

