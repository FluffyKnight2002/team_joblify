package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.config.MyUserDetails;
import com.ace_inspiration.team_joblify.dto.NotificationDto;
import com.ace_inspiration.team_joblify.entity.Notification;
import com.ace_inspiration.team_joblify.entity.NotificationUser;
import com.ace_inspiration.team_joblify.entity.Role;
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
import java.util.NoSuchElementException;
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
        if(notificationDto.getLink().contains("user-profile-edit")) {
            users = new ArrayList<>();
            users.add(userRepository.findByRole(Role.DEFAULT_HR).get());
            users.add(userRepository.findById(notificationDto.getUserId()).get());
        }

        for(User user: users) {

            NotificationUser notificationUser = new NotificationUser();
            notificationUser.setNotification(savedNotification);
            notificationUser.setSeen(false);
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
            if(!notificationDto.isDeleted()) {
                notificationDtoList.add(notificationDto);
            }
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

    @Override
    public void updateNotification(long id) {
        Optional<Notification> optionalNotification = notificationRepository.findById(id);
        Notification notification = optionalNotification.orElseThrow(() -> new NoSuchElementException("No such element found."));
        Optional<NotificationUser> optionalNotificationUser = notificationUserRepository.findByNotification(notification);
        NotificationUser notificationUser = optionalNotificationUser.orElseThrow(()-> new NoSuchElementException("No such element found"));
        notificationUser.setSeen(true);
        notificationUserRepository.save(notificationUser);
    }

    @Transactional
    @Override
    public void findDeleteAllNotificationUserByUserId(Long userId) {
        User user = User.builder().id(userId).build();
        notificationUserRepository.deleteAllByUser(user);
    }

    @Override
    public boolean findNotificationSeenByIdAndUserId(Long notificationId, Long userId) {
        Notification notification = Notification.builder().id(notificationId).build();
        User user = User.builder().id(userId).build();
        Optional<NotificationUser> optionalNotificationUser = notificationUserRepository.findNotificationUserByNotificationAndUser(notification,user);

        if (optionalNotificationUser.isPresent()) {
            NotificationUser notificationUser = optionalNotificationUser.get();
            return notificationUser.isSeen();
        }

        return true;
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

    @Override
    public void findAllNotificationUserByUserIdAndUpdate(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new NoSuchElementException("No such element found"));
        List<NotificationUser> notificationUsers = notificationUserRepository.findNotificationUserByUser(user);

        for (NotificationUser notificationUser : notificationUsers) {
            notificationUser.setSeen(true);
        }

        notificationUserRepository.saveAll(notificationUsers);

    }

    @Override
    public void findNotificationByIdAndUserIdAndUpdate(Long notificationId, Long userId) {
        Notification notification = Notification.builder().id(notificationId).build();
        User user = User.builder().id(userId).build();
        Optional<NotificationUser> optionalNotificationUser = notificationUserRepository.findNotificationUserByNotificationAndUser(notification,user);

        if (optionalNotificationUser.isPresent()) {
            NotificationUser notificationUser = optionalNotificationUser.get();
            notificationUser.setSeen(true);
            notificationUserRepository.save(notificationUser);

        }
    }

    @Override
    public void findNotificationByIdAndUserIdAndUpdate(List<Long> notificationIds, Long userId) {

    }

    public NotificationDto mapToDto(Notification notification) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        NotificationDto notificationDto = NotificationDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .link(notification.getLink())
                .isSeen(false)
                .isDeleted(false)
                .time(notification.getTime())
                .build();

        // Check if both user_id and notificationUser_id exist in the join table
        boolean bothExist = checkUserNotificationAssociation(notification.getId(), userDetails.getUserId());
        // Set isRead to false if both user_id and notification_id exist in the join table
        System.out.println("Both exit" + bothExist);
        if(bothExist == true) {
            System.out.println("Both exit.");
//            NotificationUser notificationUser =
            notificationDto.setSeen(findNotificationSeenByIdAndUserId(notification.getId(),userDetails.getUserId()));
            notificationDto.setDeleted(false);
        }else {
            notificationDto.setSeen(true);
            notificationDto.setDeleted(true);
        }

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
}
