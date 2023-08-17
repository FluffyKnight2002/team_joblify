package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.config.MyUserDetails;
import com.ace_inspiration.team_joblify.dto.NotificationDto;
import com.ace_inspiration.team_joblify.entity.Notification;
import com.ace_inspiration.team_joblify.entity.NotificationUser;
import com.ace_inspiration.team_joblify.entity.User;
import com.ace_inspiration.team_joblify.repository.NotificationRepository;
import com.ace_inspiration.team_joblify.repository.NotificationUserRepository;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import com.ace_inspiration.team_joblify.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationUserRepository notificationUserRepository;

    @Override
    public void createNotifications(NotificationDto notificationDto) {

        List<User> users = userRepository.findAll();
        Notification notification = new Notification();
        notification.setMessage(notificationDto.getMessage());
        notification.setLink(notificationDto.getLink());
        notification.setTime(notificationDto.getTime());

        Notification savedNotification = notificationRepository.save(notification);
        for(User user: users) {

            NotificationUser notificationUser = new NotificationUser();
            notificationUser.setNotification(savedNotification);
            notificationUser.setUser(user);

            notificationUserRepository.save(notificationUser);
        }

    }

    @Override
    public List<NotificationDto> showNotifications() {

        List<Notification> notificationList = notificationRepository.findAllByOrderByTimeDesc();

        // Create a list to store the mapped NotificationDto objects
        List<NotificationDto> notificationDtoList = new ArrayList<>();

        // Map each Notification entity to a NotificationDto using the NotificationService's mapToDto method
        for (Notification notification : notificationList) {
            NotificationDto notificationDto = mapToDto(notification);
            notificationDtoList.add(notificationDto);
        }

        return notificationDtoList;
    }

    @Override
    public long getNotificationCount() {
        return notificationRepository.count();
    }

    @Override
    public void removeNotification(long id) {
        Notification notification = Notification.builder().id(id).build();
        Optional<NotificationUser> optionalNotificationUser = notificationUserRepository.findByNotification(notification);
        if(optionalNotificationUser.isPresent()) {
            notificationUserRepository.deleteById(optionalNotificationUser.get().getId());
        }
        notificationRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void findDeleteAllNotificationUserByUserId(Long userId) {
        User user = User.builder().id(userId).build();
        notificationUserRepository.deleteAllByUser(user);
    }


    public NotificationDto mapToDto(Notification notification) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        NotificationDto notificationDto = NotificationDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .link(notification.getLink())
                .isSeen(false)
                .time(notification.getTime())
                .build();

        // Check if both user_id and notificationUser_id exist in the join table
        boolean bothExist = checkUserNotificationAssociation(notification.getId(), userDetails.getUserId());

        // Set isRead to false if both user_id and notification_id exist in the join table
        notificationDto.setSeen(bothExist);

        return notificationDto;
    }

    private boolean checkUserNotificationAssociation(long notificationId, long userId) {
        boolean isExit = false;
        List<NotificationUser> list = notificationUserRepository.findAll();
        if(list.isEmpty()) {
            return isExit;
        }
        for(NotificationUser notiUser: list) {
            // Assuming NotificationUser has getter methods to retrieve the notificationId and userId
            long userNotificationId = notiUser.getNotification().getId();
            long userUserId = notiUser.getUser().getId();

            // Check if the current NotificationUser entry has the matching notificationId and userId
            if (userNotificationId == notificationId && userUserId == userId) {
                // Found the association, return true
                isExit = true;
            }

        }

        return isExit;
    }


    // Method to find a notification by both notification_id and user_id
    @Override
    public void findNotificationByIdAndUserIdAndDelete(Long notificationId, Long userId) {
        Notification notification = Notification.builder().id(notificationId).build();
        User user = User.builder().id(userId).build();
        Optional<NotificationUser> optionalNotificationUser = notificationUserRepository.findNotificationUserByNotificationAndUser(notification,user);

        if (optionalNotificationUser.isPresent()) {
            NotificationUser notificationUser = optionalNotificationUser.get();
            notificationUserRepository.deleteById(notificationUser.getId());

        }
    }
}
