package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Notification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository test;
    private List<Notification> allNotification;

    @BeforeEach
    void setUp() {

        allNotification = new ArrayList<>();

        Notification notification1 = new Notification();
        LocalDateTime now = LocalDateTime.now();
        notification1.setTime(now);
        notification1.setLink("/all-user-list");
        notification1.setMessage("new user created");
        test.save(notification1);
        allNotification.add(notification1);

        Notification notification = new Notification();
        LocalDateTime now2 = LocalDateTime.now().plusMinutes(1);
        notification.setTime(now2);
        notification.setLink("/all-user-list");
        notification.setMessage("new user created");
        test.save(notification);
        allNotification.add(notification);
    }

    @AfterEach
    void tearDown() {
        test.deleteAll();
    }

    @Test
    void findAllByOrderByTimeDesc() {
        List<Notification> notificationList = test.findAllByOrderByTimeDesc();
        if(!notificationList.isEmpty()) {
            assertThat(notificationList).containsAll(allNotification);
        } else {
            assertThat(notificationList).isEmpty();
        }
    }
}