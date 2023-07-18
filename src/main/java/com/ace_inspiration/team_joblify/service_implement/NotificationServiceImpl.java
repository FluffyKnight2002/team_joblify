package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.entity.Notification;
import com.ace_inspiration.team_joblify.repository.NotificationRepository;
import com.ace_inspiration.team_joblify.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void createNotifications(Notification notification) {
        notificationRepository.save(notification);
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
