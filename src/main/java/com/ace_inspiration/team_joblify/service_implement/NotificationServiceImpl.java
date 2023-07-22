package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.dto.NotificationDto;
import com.ace_inspiration.team_joblify.entity.Notification;
import com.ace_inspiration.team_joblify.repository.NotificationRepository;
import com.ace_inspiration.team_joblify.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public Notification createNotifications(NotificationDto notificationDto) {
        Notification notification = new Notification();
        notification.setMessage(notificationDto.getMessage());
        notification.setLink(notificationDto.getLink());
        notification.setTime(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> showNotifications() {

        return notificationRepository.findAll();
    }

    @Override
    public void removeNotification(long id) {
        notificationRepository.deleteById(id);
    }
}
