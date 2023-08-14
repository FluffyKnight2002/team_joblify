package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


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


    User user = null;
    Notification notification = null;
    NotificationUser notificationUser = null;
    Department department = null;

    @BeforeEach
    void setUp() throws IOException {

        LocalDateTime now = LocalDateTime.now();

        department = Department.builder()
                .name("Human Resources")
                .build();
        departmentRepository.save(department);

        Resource resource = resourceLoader.getResource("classpath:static/assets/images/faces/5.jpg");
        InputStream inputStream = resource.getInputStream();
        byte [] photoBytes = IOUtils.toByteArray(inputStream);

        user = User.builder()
                .username("Admin")
                .name("Admin")
                .phone("09777159555")
                .email("ace@gmail.com")
                .address("Ace Data System")
                .photo(Base64.encodeBase64String(photoBytes))
                .gender(Gender.FEMALE)
                .password("1122121")
                .role(Role.DEFAULT_HR)
                .note("This is Default HR Account")
                .department(department)
                .createdDate(now)
                .lastUpdatedDate(now)
                .build();
        userRepository.save(user);

        notification = Notification.builder()
                .time(now)
                .link("/all-user-list")
                .message("new user created")
                .build();
        notificationRepository.save(notification);

        notificationUser = NotificationUser.builder()
                .user(user)
                .notification(notification)
                .build();
        notificationUserRepository.save(notificationUser);
    }

    @AfterEach
    void tearDown() {
        departmentRepository.deleteAll();
        userRepository.deleteAll();
        notificationRepository.deleteAll();
        notificationUserRepository.deleteAll();
    }

    @Test
    void deleteAllByUserTest() {
        notificationUserRepository.deleteAllByUser(user);
        Optional<NotificationUser> userNotifications = notificationUserRepository.findNotificationUserByNotificationAndUser(notification, user);
        assertThat(userNotifications).isEmpty();

    }

    @ParameterizedTest
    @MethodSource("provideNotifications")
    void findNotificationUserByNotificationAndUserTest(Notification notificationToTest, boolean shouldContain) {
        if(notificationToTest != null) {
            notificationRepository.save(notificationToTest);
            notificationUser = NotificationUser.builder()
                    .user(user)
                    .notification(notificationToTest)
                    .build();
            notificationUserRepository.save(notificationUser);
        }

        Optional<NotificationUser> userNotifications = notificationUserRepository.findNotificationUserByNotificationAndUser(notificationToTest, user);
        if (shouldContain) {
            assertThat(userNotifications).contains(notificationUser);
        } else {
            assertThat(userNotifications).isEmpty();
        }
    }



    @ParameterizedTest
    @MethodSource("provideNotifications")
    void findByNotificationTest(Notification notificationToTest, boolean shouldContain) {
        if(notificationToTest != null) {
            notificationRepository.save(notificationToTest);
            notificationUser = NotificationUser.builder()
                    .user(user)
                    .notification(notificationToTest)
                    .build();
            notificationUserRepository.save(notificationUser);
        }
        Optional<NotificationUser> nu = notificationUserRepository.findByNotification(notificationToTest);
        if (shouldContain) {
            assertThat(nu).contains(notificationUser);
        } else {
            assertThat(nu).isEmpty();
        }
    }

    static Stream<Arguments> provideNotifications() {
        LocalDateTime now = LocalDateTime.now();
        Notification existingNotification = Notification.builder()
                .time(now)
                .link("/all-user-list")
                .message("new user created")
                .build();

        // Provide your other notifications here

        return Stream.of(
                Arguments.of(existingNotification, true),  // Notification exists
                Arguments.of(null, false) // Notification doesn't exist
        );
    }
}