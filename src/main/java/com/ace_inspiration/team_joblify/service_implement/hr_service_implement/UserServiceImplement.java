package com.ace_inspiration.team_joblify.service_implement.hr_service_implement;

import com.ace_inspiration.team_joblify.dto.UserDto;
import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.ActionRepository;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import com.ace_inspiration.team_joblify.service.hr_service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@AllArgsConstructor
public class UserServiceImplement implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final ActionRepository actionRepository;

    @Override
    public User userCreate(UserDto userDto, long userId) throws IOException {

        Department department=departmentRepository.findById(userDto.getDepartment()).orElseThrow(()-> new UsernameNotFoundException("Department Not Found"));
        LocalDateTime currentDate= LocalDateTime.now();

        User user = User.builder()
                    .username(userDto.getUsername())
                    .name(userDto.getName())
                    .email(userDto.getEmail())
                    .phone(userDto.getPhone())
                    .address(userDto.getAddress())
                    .photo(Base64.getEncoder().encodeToString(userDto.getPhoto().getBytes()))
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .role(Role.DEFAULT_HR)
                    .note(userDto.getNote())
                    .department(department)
                    .createdDate(currentDate)
                    .lastUpdatedDate(currentDate)
                    .build();
        if (userDto.getGender()==1){
            User.builder()
                    .gender(Gender.FEMALE);
        } else if(userDto.getGender()==2){
            User.builder()
                    .gender(Gender.MALE);
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
