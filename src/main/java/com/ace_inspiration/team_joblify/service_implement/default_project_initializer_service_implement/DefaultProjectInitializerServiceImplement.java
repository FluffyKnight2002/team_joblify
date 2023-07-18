package com.ace_inspiration.team_joblify.service_implement.default_project_initializer_service_implement;

import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.*;
import com.ace_inspiration.team_joblify.service.default_project_initializer_service.DefaultProjectInitializerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultProjectInitializerServiceImplement implements DefaultProjectInitializerService {

    @Value("${app.default.user.password}")
    private String password;

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final ActionRepository actionRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResourceLoader resourceLoader;
    private final PositionRepository positionRepository;
    private final TechSkillsRepository techSkillsRepository;
    private final LanguageSkillsRepository languageSkillsRepository;
    @Override
    public void initialize() throws IOException {

        LocalDateTime currentDate= LocalDateTime.now();
        if(departmentRepository.count() ==0) {
            Department department = Department.builder()
                    .name("Human Resources")
                    .note("HR Department is created by Default")
                    .build();
            departmentRepository.save(department);

            Department department1 = Department.builder()
                    .name("Banking")
                    .note("Banking Department is created by Default")
                    .build();
            departmentRepository.save(department1);

            Department department2 = Department.builder()
                    .name("Retail")
                    .note("Retail Department is created by Default")
                    .build();
            departmentRepository.save(department2);
        }

        Department defaultDepartment=departmentRepository.findByName("Human Resources").orElseThrow(()-> new UsernameNotFoundException("Department Not Found"));

        if (userRepository.count() == 0) {

                Resource resource = resourceLoader.getResource("classpath:static/assets/images/faces/5.jpg");
                InputStream inputStream = resource.getInputStream();
                byte [] photoBytes = IOUtils.toByteArray(inputStream);




            User defaultUser = User.builder()
                    .username("Admin")
                    .name("Admin")
                    .phone("09777159555")
                    .email("ace@gmail.com")
                    .address("Ace Data System")
                    .photo(Base64.encodeBase64String(photoBytes))
                    .gender(Gender.FEMALE)
                    .password(passwordEncoder.encode(password))
                    .role(Role.DEFAULT_HR)
                    .note("This is Default HR Account")
                    .department(defaultDepartment)
                    .createdDate(currentDate)
                    .lastUpdatedDate(currentDate)
                    .build();
            userRepository.save(defaultUser);

            Notification notification = new Notification();
            notification.setActionName("Default HR account is created");
            notification.setActionTime(currentDate);
            notification.setMakeAsRead(false);
            notification.setUser(defaultUser);
            actionRepository.save(notification);
        }
        if(positionRepository.count() == 0){
            Position position= Position.builder()
                    .name("Junior SE Developer")
                    .build();
            positionRepository.save(position);

            Position position1= Position.builder()
                    .name("Senior SE Developer")
                    .build();
            positionRepository.save(position1);

            Position position2= Position.builder()
                    .name("Project Manager")
                    .build();
            positionRepository.save(position2);

            Position position3= Position.builder()
                    .name("Database Analysis")
                    .build();
            positionRepository.save(position3);

            Position position4= Position.builder()
                    .name("Full Stack Developer")
                    .build();
            positionRepository.save(position4);

            Position position5= Position.builder()
                    .name("Front-end Developer")
                    .build();
            positionRepository.save(position5);

            Position position6= Position.builder()
                    .name("Back-end Developer")
                    .build();
            positionRepository.save(position6);
        }
        if(techSkillsRepository.count() == 0){
            TechSkills techSkills= TechSkills.builder()
                    .name("Java")
                    .build();
            techSkillsRepository.save(techSkills);

            TechSkills techSkills1= TechSkills.builder()
                    .name("C")
                    .build();
            techSkillsRepository.save(techSkills1);

            TechSkills techSkills2= TechSkills.builder()
                    .name("C#")
                    .build();
            techSkillsRepository.save(techSkills2);

            TechSkills techSkills3= TechSkills.builder()
                    .name("C++")
                    .build();
            techSkillsRepository.save(techSkills3);

            TechSkills techSkills4= TechSkills.builder()
                    .name("Ruby")
                    .build();
            techSkillsRepository.save(techSkills4);

            TechSkills techSkills5= TechSkills.builder()
                    .name("PHP")
                    .build();
            techSkillsRepository.save(techSkills5);

            TechSkills techSkills6= TechSkills.builder()
                    .name("SQL")
                    .build();
            techSkillsRepository.save(techSkills6);
        }
        if(languageSkillsRepository.count() == 0){
            LanguageSkills languageSkills= LanguageSkills.builder()
                    .name("English")
                    .build();
            languageSkillsRepository.save(languageSkills);


            LanguageSkills languageSkills1= LanguageSkills.builder()
                    .name("Japanese")
                    .build();
            languageSkillsRepository.save(languageSkills1);

            LanguageSkills languageSkills2= LanguageSkills.builder()
                    .name("Chinese")
                    .build();
            languageSkillsRepository.save(languageSkills2);

            LanguageSkills languageSkills3= LanguageSkills.builder()
                    .name("Myanmar")
                    .build();
            languageSkillsRepository.save(languageSkills3);
        }
        log.info("Project Initialize Complete.");
    }
}
