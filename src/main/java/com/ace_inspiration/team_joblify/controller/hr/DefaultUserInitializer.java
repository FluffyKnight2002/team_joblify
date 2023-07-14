package com.ace_inspiration.team_joblify.controller.hr;


import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.ActionRepository;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class DefaultUserInitializer implements ApplicationRunner {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final ActionRepository actionRepository;
    private final PasswordEncoder passwordEncoder;


    @Value("${app.default-user-password}")
    private static String password;

    @Override
    public void run(ApplicationArguments args) throws IOException {
        LocalDateTime currentDate= LocalDateTime.now();
        Department department=departmentRepository.findById(1L).orElseThrow(()-> new UsernameNotFoundException("Department Not Found"));
        if (userRepository.count() == 0) {
            User defaultUser = User.builder()
                    .username("Admin")
                    .name("Admin")
                    .phone("09777159555")
                    .email("ace@gmail.com")
                    .address("Ace Data System")
                    .photo(photo)
                    .gender(Gender.FEMALE)
                    .password(passwordEncoder.encode(password))
                    .role(Role.DEFAULT_HR)
                    .note("This is Default HR Account")
                    .department(department)
                    .createdDate(currentDate)
                    .lastUpdatedDate(currentDate)
                    .build();
            userRepository.save(defaultUser);

            Action action=new Action();
                action.setActionName("Default HR account is created");
                action.setActionTime(currentDate);
                action.setMakeAsRead(false);
                action.setUser(defaultUser);
            actionRepository.save(action);
        }
    }
}

