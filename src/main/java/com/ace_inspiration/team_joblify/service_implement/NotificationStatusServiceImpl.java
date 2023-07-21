package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.dto.NotificationDto;
import com.ace_inspiration.team_joblify.entity.NotificationStatus;
import com.ace_inspiration.team_joblify.repository.NotificationStatusRepository;
import com.ace_inspiration.team_joblify.service.NotificationService;
import com.ace_inspiration.team_joblify.service.NotificationStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationStatusServiceImpl implements NotificationStatusService {

    private final NotificationStatusRepository notificationStatusRepository;
    private final NotificationService notificationService;

    @Override
    public NotificationStatus createNotification(NotificationStatus notificationStatus) {
        notificationStatus.setNotification(
                notificationService.createNotifications(notificationStatus.getNotification())
        );
        return notificationStatusRepository.save(notificationStatus);
    }

    @Override
    public List<NotificationDto> selectAllNotificationStatus() {
        List<NotificationDto> notificationDtos = new ArrayList<>();
        List<NotificationStatus> notifications = notificationStatusRepository.findAll();

        for (NotificationStatus notification : notifications) {
            NotificationDto dto = entityToDto(notification);
            notificationDtos.add(dto);
        }

        return notificationDtos;
    }

    private NotificationDto entityToDto(NotificationStatus notification) {
        NotificationDto dto = NotificationDto.builder()
                .id(notification.getId())
                .name(notification.getNotification().getName())
                .link(notification.getNotification().getLink())
                .makeAsRead(notification.isMakeAsRead())
                .time(notification.getNotification().getTime())
                .user(notification.getUser())
                .build();

        return dto;
    }

    @Override
    public void deleteNotificationById(long id) {
        notificationStatusRepository.deleteById(id);
    }
}
