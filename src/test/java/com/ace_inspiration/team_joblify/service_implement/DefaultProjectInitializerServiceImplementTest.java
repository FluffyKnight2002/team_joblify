package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.*;

import com.ace_inspiration.team_joblify.service_implement.DefaultProjectInitializerServiceImplement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class DefaultProjectInitializerServiceImplementTest{
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
    @ValueSource(longs = {0L, 1L})
    void initialize(long count) throws IOException {

        Department department = Department.builder()
                .name("Human Resources")
                .build();

        when(departmentRepository.count()).thenReturn(count);
        if(count == 0L) {
           when(departmentRepository.save(department)).thenReturn(null);
        }
        when(departmentRepository.findByName("Human Resources"))
                .thenReturn(Optional.of(department));


        when(userRepository.count()).thenReturn(0L);

        Resource resource = new ClassPathResource("static/assets/images/faces/5.jpg");
        when(resourceLoader.getResource("classpath:static/assets/images/faces/5.jpg"))
                .thenReturn(resource);

        InputStream inputStream = resource.getInputStream();
        byte [] photoBytes = IOUtils.toByteArray(inputStream);

        // Mock userRepository

        when(userRepository.save(any(User.class)))
                .thenReturn(null);

        // Mock notificationRepository
        when(notificationRepository.save(any(Notification.class)))
                .thenReturn(null);

        // Mock notificationUserRepository
        when(notificationUserRepository.save(any(NotificationUser.class)))
                .thenReturn(null);

        // Mock positionRepository
        when(positionRepository.count()).thenReturn(0L);
        when(positionRepository.save(any(Position.class)))
                .thenReturn(null);

        // Mock techSkillsRepository
        when(techSkillsRepository.count()).thenReturn(0L);
        when(techSkillsRepository.save(any(TechSkills.class)))
                .thenReturn(null);

        // Mock languageSkillsRepository
        when(languageSkillsRepository.count()).thenReturn(0L);
        when(languageSkillsRepository.save(any(LanguageSkills.class)))
                .thenReturn(null);

        implement.initialize();

        verify(departmentRepository, times(1)).findByName("Human Resources");
        verify(departmentRepository, times(1)).count();
        verify(departmentRepository, times(count == 0L ? 1 : 0)).save(any(Department.class));



        verify(userRepository, times(1)).count();
        verify(userRepository, times(1)).save(any(User.class));

        verify(notificationRepository, times(1)).save(any(Notification.class));

        verify(notificationUserRepository, times(1)).save(any(NotificationUser.class));

        verify(positionRepository, times(1)).count();
        verify(positionRepository, times(7)).save(any(Position.class));

        verify(techSkillsRepository, times(1)).count();
        verify(techSkillsRepository, times(7)).save(any(TechSkills.class));

        verify(languageSkillsRepository, times(1)).count();
        verify(languageSkillsRepository, times(4)).save(any(LanguageSkills.class));
    }





}
