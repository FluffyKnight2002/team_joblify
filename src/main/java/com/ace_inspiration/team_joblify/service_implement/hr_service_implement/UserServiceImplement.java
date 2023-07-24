package com.ace_inspiration.team_joblify.service_implement.hr_service_implement;

import com.ace_inspiration.team_joblify.dto.UserDto;
import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.repository.NotificationRepository;
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
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final NotificationRepository notificationRepository;

    @Value("${app.default.user.password}")
    private String password;
    @Override
    public User userCreate(UserDto userDto, long userId) throws IOException {
        Department department= departmentRepository.findByName(userDto.getDepartment()).orElse(null);
        if(department == null){
            department = new Department();
            department.setName(userDto.getDepartment());
            departmentRepository.save(department);
        }

        LocalDateTime currentDate= LocalDateTime.now();

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());
        user.setPhoto(Base64.getEncoder().encodeToString(userDto.getPhoto().getBytes()));
        user.setPassword(passwordEncoder.encode(password));


        if(userDto.getRole()==1){
            user.setRole(Role.SENIOR_HR);
        } else if(userDto.getRole()==2){
            user.setRole(Role.JUNIOR_HR);
        } else if(userDto.getRole()==3){
            user.setRole(Role.MANAGEMENT);
        } else if(userDto.getRole()==4){
            user.setRole(Role.INTERVIEWER);
        } else if(userDto.getRole()==5){
            user.setRole(Role.OTHER);
        }

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


        Notification notification =new Notification();

        User actionUser=userRepository.findById(userId)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found"));
        notification.setMessage(userDto.getName() + " is created by "+ actionUser.getName());
        notification.setTime(currentDate);
        notification.setLink("aaa");
        notificationRepository.save(notification);

        return null;

        }

    @Override
    public Optional<User> findById(long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public void savePassword(String password, long userId) {
        User user=userRepository.findById(userId).orElseThrow(()-> new NoSuchElementException("User Not Found"));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public User adminProfileEdit(UserDto userDto, long userId) throws  IOException {
        LocalDateTime currentDate = LocalDateTime.now();

        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User Not Found"));
        Department department= departmentRepository.findByName(userDto.getDepartment()).orElse(null);
        if(department == null){
            department = new Department();
            department.setName(userDto.getDepartment());
            departmentRepository.save(department);
        }

        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());
        user.setPhoto(Base64.getEncoder().encodeToString(userDto.getPhoto().getBytes()));
        user.setPassword(passwordEncoder.encode(password));

        if (userDto.getRole() == 1) {
            user.setRole(Role.SENIOR_HR);
        } else if (userDto.getRole() == 2) {
            user.setRole(Role.JUNIOR_HR);
        } else if (userDto.getRole() == 3) {
            user.setRole(Role.MANAGEMENT);
        } else if (userDto.getRole() == 4) {
            user.setRole(Role.INTERVIEWER);
        } else if(userDto.getRole()==5){
            user.setRole(Role.OTHER);
        }

        user.setNote(userDto.getNote());
        user.setDepartment(department);
        user.setLastUpdatedDate(currentDate);

        if (userDto.getGender() == 1) {
            user.setGender(Gender.FEMALE);
        } else if (userDto.getGender() == 2) {
            user.setGender(Gender.MALE);
        } else if (userDto.getGender() == 3) {
            user.setGender(Gender.OTHER);
        }

        return userRepository.save(user);
    }

    @Override
    public User userProfileEdit(UserDto userDto, long userId) throws IOException {

        LocalDateTime currentDate = LocalDateTime.now();

        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User Not Found"));

        Department department= departmentRepository.findByName(userDto.getDepartment()).orElse(null);
        if(department == null){
            department = new Department();
            department.setName(userDto.getDepartment());
            departmentRepository.save(department);
        }

        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());
        user.setPhoto(Base64.getEncoder().encodeToString(userDto.getPhoto().getBytes()));

        if (userDto.getRole() == 1) {
            user.setRole(Role.SENIOR_HR);
        } else if (userDto.getRole() == 2) {
            user.setRole(Role.JUNIOR_HR);
        } else if (userDto.getRole() == 3) {
            user.setRole(Role.MANAGEMENT);
        } else if (userDto.getRole() == 4) {
            user.setRole(Role.INTERVIEWER);
        } else if(userDto.getRole()==5){
            user.setRole(Role.OTHER);
        }

        user.setNote(userDto.getNote());
        user.setDepartment(department);
        user.setLastUpdatedDate(currentDate);

        if (userDto.getGender() == 1) {
            user.setGender(Gender.FEMALE);
        } else if (userDto.getGender() == 2) {
            user.setGender(Gender.MALE);
        } else if (userDto.getGender() == 3) {
            user.setGender(Gender.OTHER);
        }

        return userRepository.save(user);
    }
}
