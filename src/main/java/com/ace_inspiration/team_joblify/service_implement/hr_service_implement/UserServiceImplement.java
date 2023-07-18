package com.ace_inspiration.team_joblify.service_implement.hr_service_implement;

import com.ace_inspiration.team_joblify.dto.UserDto;
import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.ActionRepository;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import com.ace_inspiration.team_joblify.service.hr_service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {



    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final ActionRepository actionRepository;

    @Value("${app.default.user.password}")
    private String password;
    @Override
    public User userCreate(UserDto userDto, long userId) throws IOException {

        Department department=departmentRepository.findByName(userDto.getDepartment()).orElseThrow(()-> new UsernameNotFoundException("Department Not Found"));
        LocalDateTime currentDate= LocalDateTime.now();

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());
        user.setPhoto(Base64.getEncoder().encodeToString(userDto.getPhoto().getBytes()));
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.DEFAULT_HR);
        user.setNote(userDto.getNote());
        user.setDepartment(department);
        user.setCreatedDate(currentDate);
        user.setLastUpdatedDate(currentDate);

        if (userDto.getGender() == 1) {
            user.setGender(Gender.FEMALE);
        } else if (userDto.getGender() == 2) {
            user.setGender(Gender.MALE);
        } else if (userDto.getGender() == 3) {
            user.setGender(Gender.OTHER);
        }

        userRepository.save(user);


        Action action=new Action();

        User actionUser=userRepository.findById(userId).orElseThrow(()-> new UsernameNotFoundException("User Not Found"));
        action.setActionName(userDto.getName() + " is created by "+ actionUser.getName());
        action.setActionTime(currentDate);
        action.setMakeAsRead(false);
        action.setUser(user);
        actionRepository.save(action);

        return null;

        }
    }
