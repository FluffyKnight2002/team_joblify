package com.ace_inspiration.team_joblify.controller.hr;


import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.ActionRepository;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import jakarta.servlet.ServletContext;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class DefaultUserInitializer implements ApplicationRunner {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final ActionRepository actionRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServletContext servletContext;



    @Value("${app.default-user-password}")
    private static String password;



    @Override
    public void run(ApplicationArguments args) throws IOException {

        String fullPath = servletContext.getRealPath("/static/assets/images/faces/5.jpg");
        File file = new File(fullPath);
        byte[] fileBytes;
        try (InputStream inputStream = new FileInputStream(file)) {
            fileBytes = StreamUtils.copyToByteArray(inputStream);
        }

        LocalDateTime currentDate= LocalDateTime.now();

        Department department=departmentRepository.findById(1L).orElseThrow(()-> new UsernameNotFoundException("Department Not Found"));
        if (userRepository.count() == 0) {
            User defaultUser = User.builder()
                    .username("Admin")
                    .name("Admin")
                    .phone("09777159555")
                    .email("ace@gmail.com")
                    .address("Ace Data System")
                    .photo(Base64.encodeBase64String(fileBytes))
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

