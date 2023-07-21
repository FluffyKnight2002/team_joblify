package com.ace_inspiration.team_joblify.controller.websocket;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public NotificationController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/application")
    @SendTo("/all/notification")
    public Message send(final Message message) throws Exception {
        return message;

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

