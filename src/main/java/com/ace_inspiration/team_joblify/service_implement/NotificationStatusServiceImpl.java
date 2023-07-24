package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.dto.NotificationDto;
import com.ace_inspiration.team_joblify.entity.NotificationStatus;
import com.ace_inspiration.team_joblify.repository.NotificationStatusRepository;
import com.ace_inspiration.team_joblify.service.NotificationService;
import com.ace_inspiration.team_joblify.service.NotificationStatusService;
import com.ace_inspiration.team_joblify.service.hr_service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationStatusServiceImpl implements NotificationStatusService {

    private final NotificationStatusRepository notificationStatusRepository;
    private final NotificationService notificationService;
    private final UserService userService;

    @Override
    public void createNotification(NotificationDto notificationDto) {
        NotificationStatus notificationStatus = dtoToEntry(notificationDto);
        notificationStatusRepository.save(notificationStatus);
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

        return NotificationDto.builder()
                .id(notification.getId())
                .message(notification.getNotification().getMessage())
                .link(notification.getNotification().getLink())
                .makeAsRead(notification.isMakeAsRead())
                .time(notification.getNotification().getTime())
                .user(notification.getUser())
                .build();
    }

    private NotificationStatus dtoToEntry(NotificationDto dto) {
        return NotificationStatus.builder()
                .notification(notificationService.createNotifications(dto))
                .makeAsRead(dto.isMakeAsRead())
                .user(dto.getUser())
                .build();
    }

    @Override
    public void deleteNotificationById(long id) {
        notificationStatusRepository.deleteById(id);
    }

    @Override
    public long setRead(long notificationId,long userId) {

//
        return 1;
    }
}
