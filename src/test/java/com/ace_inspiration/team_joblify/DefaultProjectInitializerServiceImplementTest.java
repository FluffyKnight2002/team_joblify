package com.ace_inspiration.team_joblify;

import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.*;
import com.ace_inspiration.team_joblify.service_implement.default_project_initializer_service_implement.DefaultProjectInitializerServiceImplement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
class DefaultProjectInitializerServiceImplementTest{
        @InjectMocks
        private DefaultProjectInitializerServiceImplement implement;

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

        @Test
        void initialize() throws IOException {
                LocalDateTime currentDate= LocalDateTime.now();
                if(departmentRepository.count() ==0) {
                        verify(departmentRepository,times(1)).count();
                        Department department = Department.builder()
                                .name("Human Resources")
                                .note("HR Department is created by Default")
                                .build();
                        departmentRepository.save(department);
                        verify(departmentRepository,times(1)).save(department);

                        Department department1 = Department.builder()
                                .name("Banking")
                                .note("Banking Department is created by Default")
                                .build();
                        departmentRepository.save(department1);
                        verify(departmentRepository,times(1)).save(department1);

                        Department department2 = Department.builder()
                                .name("Retail")
                                .note("Retail Department is created by Default")
                                .build();
                        departmentRepository.save(department2);
                        verify(departmentRepository,times(1)).save(department2);
                }
                Department department=new Department();
                department.setName("Human Resources");
                department.setNote("HR Department is created by Default");
                when(departmentRepository.findByName("Human Resources")).thenReturn(Optional.of(department));
                Department defaultDepartment=departmentRepository.findByName("Human Resources").orElseThrow(()-> new UsernameNotFoundException("Department Not Found"));

                if (userRepository.count() == 0) {
                        verify(userRepository,times(1)).count();
                        Resource resource = new ClassPathResource("static/assets/images/faces/5.jpg");
                        InputStream inputStream = resource.getInputStream();
                        byte[] fileBytes = StreamUtils.copyToByteArray(inputStream);
                        inputStream.close();

                        User defaultUser = User.builder()
                                .username("Admin")
                                .name("Admin")
                                .phone("09777159555")
                                .email("ace@gmail.com")
                                .address("Ace Data System")
                                .photo(Base64.encodeBase64String(fileBytes))
                                .gender(Gender.FEMALE)
                                .password(passwordEncoder.encode("password"))
                                .role(Role.DEFAULT_HR)
                                .note("This is Default HR Account")
                                .department(defaultDepartment)
                                .createdDate(currentDate)
                                .lastUpdatedDate(currentDate)
                                .build();
                        userRepository.save(defaultUser);
                        verify(userRepository,times(1)).save(defaultUser);

                        Notification notification =new Notification();
                        notification.setName("Default HR account is created");
                        notification.setTime(currentDate);
                        notification.setUser(defaultUser);
                        notificationRepository.save(notification);
                        verify(notificationRepository,times(1)).save(notification);
                }
                if(positionRepository.count() == 0){
                        verify(userRepository,times(1)).count();
                        Position position= Position.builder()
                                .name("Junior SE Developer")
                                .build();
                        positionRepository.save(position);
                        verify(positionRepository,times(1)).save(position);

                        Position position1= Position.builder()
                                .name("Senior SE Developer")
                                .build();
                        positionRepository.save(position1);
                        verify(positionRepository,times(1)).save(position1);

                        Position position2= Position.builder()
                                .name("Project Manager")
                                .build();
                        positionRepository.save(position2);
                        verify(positionRepository,times(1)).save(position2);

                        Position position3= Position.builder()
                                .name("Database Analysis")
                                .build();
                        positionRepository.save(position3);
                        verify(positionRepository,times(1)).save(position3);

                        Position position4= Position.builder()
                                .name("Full Stack Developer")
                                .build();
                        positionRepository.save(position4);
                        verify(positionRepository,times(1)).save(position4);

                        Position position5= Position.builder()
                                .name("Front-end Developer")
                                .build();
                        positionRepository.save(position5);
                        verify(positionRepository,times(1)).save(position5);

                        Position position6= Position.builder()
                                .name("Back-end Developer")
                                .build();
                        positionRepository.save(position6);
                        verify(positionRepository,times(1)).save(position6);
                }
                if(techSkillsRepository.count() == 0){
                        verify(techSkillsRepository,times(1)).count();

                        TechSkills techSkills= TechSkills.builder()
                                .name("Java")
                                .build();
                        techSkillsRepository.save(techSkills);
                        verify(techSkillsRepository,times(1)).save(techSkills);

                        TechSkills techSkills1= TechSkills.builder()
                                .name("C")
                                .build();
                        techSkillsRepository.save(techSkills1);
                        verify(techSkillsRepository,times(1)).save(techSkills1);

                        TechSkills techSkills2= TechSkills.builder()
                                .name("C#")
                                .build();
                        techSkillsRepository.save(techSkills2);
                        verify(techSkillsRepository,times(1)).save(techSkills2);

                        TechSkills techSkills3= TechSkills.builder()
                                .name("C++")
                                .build();
                        techSkillsRepository.save(techSkills3);
                        verify(techSkillsRepository,times(1)).save(techSkills3);

                        TechSkills techSkills4= TechSkills.builder()
                                .name("PHP")
                                .build();
                        techSkillsRepository.save(techSkills4);
                        verify(techSkillsRepository,times(1)).save(techSkills4);

                        TechSkills techSkills5= TechSkills.builder()
                                .name("Ruby")
                                .build();
                        techSkillsRepository.save(techSkills5);
                        verify(techSkillsRepository,times(1)).save(techSkills5);

                        TechSkills techSkills6= TechSkills.builder()
                                .name("SQL")
                                .build();
                        techSkillsRepository.save(techSkills6);
                        verify(techSkillsRepository,times(1)).save(techSkills6);
                }
                if(languageSkillsRepository.count() == 0){
                        verify(languageSkillsRepository,times(1)).count();

                        LanguageSkills languageSkills= LanguageSkills.builder()
                                .name("English")
                                .build();
                        languageSkillsRepository.save(languageSkills);
                        verify(languageSkillsRepository,times(1)).save(languageSkills);

                        LanguageSkills languageSkills1= LanguageSkills.builder()
                                .name("Japanese")
                                .build();
                        languageSkillsRepository.save(languageSkills1);
                        verify(languageSkillsRepository,times(1)).save(languageSkills1);

                        LanguageSkills languageSkills2= LanguageSkills.builder()
                                .name("Chinese")
                                .build();
                        languageSkillsRepository.save(languageSkills2);
                        verify(languageSkillsRepository,times(1)).save(languageSkills2);

                        LanguageSkills languageSkills3= LanguageSkills.builder()
                                .name("Myanmar")
                                .build();
                        languageSkillsRepository.save(languageSkills3);
                        verify(languageSkillsRepository,times(1)).save(languageSkills3);
                }
                log.info("Project Initialize Complete.");
        }





}
