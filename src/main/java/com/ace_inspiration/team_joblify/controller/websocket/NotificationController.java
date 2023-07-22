package com.ace_inspiration.team_joblify.controller.websocket;

import com.ace_inspiration.team_joblify.config.MyUserDetails;
import com.ace_inspiration.team_joblify.dto.NotificationDto;
import com.ace_inspiration.team_joblify.service.NotificationStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {

//    private final SimpMessagingTemplate simpMessagingTemplate;
    private final List<NotificationDto> notifications = new ArrayList<>();
    private final NotificationStatusService notificationStatusService;

//    public NotificationController(SimpMessagingTemplate simpMessagingTemplate) {
//        this.simpMessagingTemplate = simpMessagingTemplate;
//    }

    @MessageMapping("/notification")
    @SendTo("/topic/allNotification")
    public List<NotificationDto> getAllNotifications(){
        return notificationStatusService.selectAllNotificationStatus();
    }

//    @MessageMapping("/application")
//    @SendTo("/all/notification")
//    public Message send(final Message message) throws Exception {
//        return message;
//
//    }

    @MessageMapping("/makeAsRead/{id}")
    @SendTo("/topic/unreadEvent")
    public Long makeNotificationAsRead(@PathVariable("id")Long id, Authentication authentication){
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        NotificationDto notificationDto = notifications.stream()
                .filter(n-> (n.getId() == id && n.getUser().getId() == myUserDetails.getUserId()))
                .findFirst()
                .orElse(null);

        if(notificationDto != null) {
            return notificationStatusService.setRead(notificationDto.getId(),myUserDetails.getUserId());
        }
        return null;
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

