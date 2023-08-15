package com.ace_inspiration.team_joblify.service_implement.hr_service_implement;

import com.ace_inspiration.team_joblify.dto.UserDto;
import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.repository.NotificationRepository;
import com.ace_inspiration.team_joblify.repository.NotificationUserRepository;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import com.ace_inspiration.team_joblify.service.hr_service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationUserRepository notificationUserRepository;

    @Value("${app.default.user.password}")
    private String password;

    private String notFound= "User Not Found";
    @Override
    public User userCreate(UserDto userDto, long userId) throws IOException {
        Department department = departmentRepository.findByName(userDto.getDepartment()).orElse(null);
        if (department == null) {
            department = new Department();
            department.setName(userDto.getDepartment());
            departmentRepository.save(department);
        }

        LocalDateTime currentDate = LocalDateTime.now();

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());
        user.setPhoto(Base64.getEncoder().encodeToString(userDto.getPhoto().getBytes()));
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.valueOf(userDto.getRole()));
        user.setNote(userDto.getNote());
        user.setAccountStatus(true);
        user.setDepartment(department);
        user.setCreatedDate(currentDate);
        user.setLastUpdatedDate(currentDate);
        user.setGender(Gender.valueOf(userDto.getGender()));

        userRepository.save(user);

        Notification notification = new Notification();

        
        notification.setMessage(user.getName() + " is created by Admin");
        notification.setTime(currentDate);
        notification.setLink("/user-profile-edit?id="+ user.getId());
        notificationRepository.save(notification);

        NotificationUser notificationUser = NotificationUser.builder()
        .notification(notification)
        .user(user)
        .build();
        notificationUserRepository.save(notificationUser);

        return user;

    }

    @Override
    public Optional<User> findById(long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public void savePassword(String password, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(notFound));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public User adminProfileEdit(UserDto userDto, long userId) throws IOException {
        LocalDateTime currentDate = LocalDateTime.now();

        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(notFound));
        Department department = departmentRepository.findByName(userDto.getDepartment()).orElse(null);
        if (department == null) {
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
        user.setRole(Role.valueOf(userDto.getRole()));
        user.setNote(userDto.getNote());
        user.setDepartment(department);
        user.setLastUpdatedDate(currentDate);
        user.setGender(Gender.valueOf(userDto.getGender()));

        return userRepository.save(user);
    }

    @Override
    public User userProfileEdit(UserDto userDto, long userId) throws IOException {

        LocalDateTime currentDate = LocalDateTime.now();

        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(notFound));

        Department department = departmentRepository.findByName(userDto.getDepartment()).orElse(null);
        if (department == null) {
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
        user.setRole(Role.valueOf(userDto.getRole()));
        user.setNote(userDto.getNote());
        user.setDepartment(department);
        user.setAccountStatus(userDto.isAccountStatus());
        user.setLastUpdatedDate(currentDate);
        user.setGender(Gender.valueOf(userDto.getGender()));

        return userRepository.save(user);
    }

    @Override
    public boolean emailDuplication(String email) {
        User user = userRepository.findByEmail(email).orElse(null);

        return user != null;
    }

    @Override
    public boolean emailDuplicationExceptMine(String email, long userId) {
        List<User> user = userRepository.findByEmailAndIdNot(email, userId);

        return !user.isEmpty();
    }

    @Override
    public boolean checkOldPassword(String password, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new NoSuchElementException("User not Found."));
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public boolean passwordChange(String newPassword, String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("User not Found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean suspend(long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new NoSuchElementException("User Not Found."));
        user.setAccountStatus(false);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean activate(long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new NoSuchElementException("User Not Found."));
        user.setAccountStatus(true);
        userRepository.save(user);
        return true;
    }
}
