package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.*;

import com.ace_inspiration.team_joblify.service_implement.DefaultProjectInitializerServiceImplement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultProjectInitializerServiceImplementTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private TechSkillsRepository techSkillsRepository;

    @Mock
    private LanguageSkillsRepository languageSkillsRepository;

    @Mock
    private NotificationUserRepository notificationUserRepository;

    @Mock
    private ResourceLoader resourceLoader;

    private DefaultProjectInitializerServiceImplement implement;

    @BeforeEach
    void setUp() {
        implement = new DefaultProjectInitializerServiceImplement(
                departmentRepository, userRepository, notificationRepository, passwordEncoder,
                resourceLoader, positionRepository, techSkillsRepository, languageSkillsRepository,
                notificationUserRepository);
    }

    @ParameterizedTest
    @MethodSource("initializeParameters")
    void initialize(Department department, User user, long positionCount, long techSkillsCount, long languageSkillsCount) throws IOException {

        Department d = Department.builder()
                .name("Human Resources")
                .build();


        when(departmentRepository.findByName("Human Resources")).thenReturn(Optional.ofNullable(department));
        if (department == null) {
            when(departmentRepository.save(d)).thenReturn(null);
        }


        when(userRepository.findByRole(Role.DEFAULT_HR)).thenReturn(Optional.ofNullable(user));
        if (user == null) {


            // Mock userRepository
            when(userRepository.save(any(User.class)))
                    .thenReturn(null);

            // Mock notificationRepository
            when(notificationRepository.save(any(Notification.class)))
                    .thenReturn(null);

            // Mock notificationUserRepository
            when(notificationUserRepository.save(any(NotificationUser.class)))
                    .thenReturn(null);
        }

        // Mock positionRepository
        when(positionRepository.count()).thenReturn(positionCount);

        if (positionCount == 0) {
            when(positionRepository.save(any(Position.class)))
                    .thenReturn(null);
        }


        // Mock techSkillsRepository
        when(techSkillsRepository.count()).thenReturn(techSkillsCount);

        if (techSkillsCount == 0) {
            when(techSkillsRepository.save(any(TechSkills.class)))
                    .thenReturn(null);
        }

        // Mock languageSkillsRepository
        when(languageSkillsRepository.count()).thenReturn(languageSkillsCount);

        if (languageSkillsCount == 0) {
            when(languageSkillsRepository.save(any(LanguageSkills.class)))
                    .thenReturn(null);
        }
        implement.initialize();

        verify(departmentRepository, times(1)).findByName(anyString());
        verify(departmentRepository, times(department == null ? 1 : 0)).save(any(Department.class));

        verify(userRepository, times(1)).findByRole(Role.DEFAULT_HR);
        verify(userRepository, times(user == null ? 1 : 0)).save(any(User.class));

        verify(notificationRepository, times(user == null ? 1 : 0)).save(any(Notification.class));

        verify(notificationUserRepository, times(user == null ? 1 : 0)).save(any(NotificationUser.class));

        verify(positionRepository, times(1)).count();
        verify(positionRepository, times(positionCount == 0L ? 7 : 0)).save(any(Position.class));

        verify(techSkillsRepository, times(1)).count();
        verify(techSkillsRepository, times(techSkillsCount == 0L ? 7 : 0)).save(any(TechSkills.class));

        verify(languageSkillsRepository, times(1)).count();
        verify(languageSkillsRepository, times(languageSkillsCount == 0L ? 4 : 0)).save(any(LanguageSkills.class));
    }


    static Stream<Arguments> initializeParameters() {
        Department d = Department.builder()
                .name("Human Resources")
                .build();


        User u = User.builder()
                .name("Admin")
                .username("Admin")
                .build();

        return Stream.of(
                Arguments.of(null, null, 0L, 0L, 0L), // 1
                Arguments.of(null, null, 0L, 0L, 1L), // 2
                Arguments.of(null, null, 0L, 1L, 0L), // 3
                Arguments.of(null, null, 0L, 1L, 1L), // 4
                Arguments.of(null, null, 1L, 0L, 0L), // 5
                Arguments.of(null, null, 1L, 0L, 1L), // 6
                Arguments.of(null, null, 1L, 1L, 0L), // 7
                Arguments.of(null, null, 1L, 1L, 1L), // 8

                Arguments.of(null, u, 0L, 0L, 0L),   // 9
                Arguments.of(null, u, 0L, 0L, 1L),   // 10
                Arguments.of(null, u, 0L, 1L, 0L),   // 11
                Arguments.of(null, u, 0L, 1L, 1L),   // 12
                Arguments.of(null, u, 1L, 0L, 0L),   // 13
                Arguments.of(null, u, 1L, 0L, 1L),   // 14
                Arguments.of(null, u, 1L, 1L, 0L),   // 15
                Arguments.of(null, u, 1L, 1L, 1L),   // 16

                Arguments.of(d, null, 0L, 0L, 0L),   // 17
                Arguments.of(d, null, 0L, 0L, 1L),   // 18
                Arguments.of(d, null, 0L, 1L, 0L),   // 19
                Arguments.of(d, null, 0L, 1L, 1L),   // 20
                Arguments.of(d, null, 1L, 0L, 0L),   // 21
                Arguments.of(d, null, 1L, 0L, 1L),   // 22
                Arguments.of(d, null, 1L, 1L, 0L),   // 23
                Arguments.of(d, null, 1L, 1L, 1L),   // 24

                Arguments.of(d, u, 0L, 0L, 0L), // 25
                Arguments.of(d, u, 0L, 0L, 1L), // 26
                Arguments.of(d, u, 0L, 1L, 0L), // 27
                Arguments.of(d, u, 0L, 1L, 1L), // 28
                Arguments.of(d, u, 1L, 0L, 0L), // 29
                Arguments.of(d, u, 1L, 0L, 1L), // 30
                Arguments.of(d, u, 1L, 1L, 0L), // 31
                Arguments.of(d, u, 1L, 1L, 1L)  // 32

                // Add more sets of values as needed
        );
    }


}
