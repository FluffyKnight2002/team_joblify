package com.ace_inspiration.team_joblify.controller.websocket;

import com.ace_inspiration.team_joblify.config.MyUserDetails;
import com.ace_inspiration.team_joblify.dto.NotificationDto;
import com.ace_inspiration.team_joblify.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/notifications")
public class NotificationController {

    private final List<NotificationDto> notifications = new ArrayList<>();
    private final NotificationService notificationService;

    @GetMapping("/show")
    public List<NotificationDto> getAllNotifications(){
        List<NotificationDto> notificationDtos = notificationService.showNotifications();
        if(notificationDtos.isEmpty()) {
            return new ArrayList<>();
        }
        return notificationDtos;
    }

    @GetMapping("/count")
    public long getNotificationsCount(){
        return notificationService.getNotificationCount();
    }


    @GetMapping("/delete-notifications")
    public void deleteNotification(@RequestParam("notifications") List<Long> notificationIds, Authentication authentication){
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        // Find the notification by both notification_id and user_id
        System.out.println("Reach backend!!!");
        for (Long id : notificationIds) {
            System.out.println("Notification id: " + id);
            notificationService.findNotificationByIdAndUserIdAndDelete(id, myUserDetails.getUserId());
        }
    }

    @GetMapping("/delete-all-notification")
    public void deleteAllNotification(Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        notificationService.findDeleteAllNotificationUserByUserId(myUserDetails.getUserId());
    }

    @GetMapping("/make-as-read")
    public void makeNotificationAsRead(@RequestParam("id")Long notificationId, Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        System.out.println("notification id : " + notificationId + " " + "user id : " + myUserDetails.getUserId());
        notificationService.findNotificationByIdAndUserIdAndUpdate(notificationId,myUserDetails.getUserId());
    }

    @GetMapping("/make-all-as-read")
    public void makeAllNotificationAsRead(Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        notificationService.findAllNotificationUserByUserIdAndUpdate(myUserDetails.getUserId());
    }

}

