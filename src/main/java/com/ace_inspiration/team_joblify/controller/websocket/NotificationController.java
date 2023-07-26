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

//    private final SimpMessagingTemplate simpMessagingTemplate;
    private final List<NotificationDto> notifications = new ArrayList<>();
    private final NotificationService notificationService;

//    public NotificationController(SimpMessagingTemplate simpMessagingTemplate) {
//        this.simpMessagingTemplate = simpMessagingTemplate;
//    }

    @GetMapping("/show")
    public List<NotificationDto> getAllNotifications(){
        List<NotificationDto> notifications = notificationService.showNotifications();
        return notifications;
    }

    @GetMapping("/count")
    public long getNotificationsCount(){
        return notificationService.getNotificationCount();
    }

//    @MessageMapping("/application")
//    @SendTo("/all/notification")
//    public Message send(final Message message) throws Exception {
//        return message;
//
//    }

    @GetMapping("/makeAsRead")
    public void makeNotificationAsRead(@RequestParam("id")Long notificationId, Authentication authentication){
        System.out.println("NotificationId : " + notificationId);
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

//    @MessageMapping("/private")
//    public void sendToSpecificUser(@Payload Message message) {
//        simpMessagingTemplate.convertAndSendToUser(message.getClass().getName(), "/toDefaultHR", message);
//    }

//    @MessageMapping("/private")
//    public void sendToSpecificUser(@Payload Message<String> message, SimpMessageHeaderAccessor accessor) {
//        String destination = "/user/" + accessor.getUser().getName() + "/toDefaultHR";
//        simpMessagingTemplate.convertAndSend(destination, message);
//    }
}

