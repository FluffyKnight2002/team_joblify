package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class NotificationUserRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationUserRepository notificationUserRepository;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() throws IOException {

        LocalDateTime now = LocalDateTime.now();

        Department department = Department.builder()
                .name("Human Resources")
                .build();
        departmentRepository.save(department);

        Resource resource = resourceLoader.getResource("classpath:static/assets/images/faces/5.jpg");
        InputStream inputStream = resource.getInputStream();
        byte [] photoBytes = IOUtils.toByteArray(inputStream);

        User user = User.builder()
                .username("Admin")
                .name("Admin")
                .phone("09777159555")
                .email("ace@gmail.com")
                .address("Ace Data System")
                .photo(Base64.encodeBase64String(photoBytes))
                .gender(Gender.FEMALE)
                .password(passwordEncoder.encode("1122121"))
                .role(Role.DEFAULT_HR)
                .note("This is Default HR Account")
                .department(department)
                .createdDate(now)
                .lastUpdatedDate(now)
                .build();
        userRepository.save(user);

        Notification notification = Notification.builder()
                .time(now)
                .link("/all-user-list")
                .message("new user created")
                .build();
        notificationRepository.save(notification);

        NotificationUser notificationUser = NotificationUser.builder()
                .user(user)
                .notification(notification)
                .build();
    }

    @AfterEach
    void tearDown() {
        departmentRepository.deleteAll();
        userRepository.deleteAll();
        notificationRepository.deleteAll();
        notificationUserRepository.deleteAll();
    }

    @Test
    void deleteAllByUser() {
    }

    @Test
    void findNotificationUserByNotificationAndUser() {
    }

    @Test
    void findByNotification() {
    }
}