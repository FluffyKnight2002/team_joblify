package com.ace_inspiration.team_joblify.service_implement.hr_service_implement;

import com.ace_inspiration.team_joblify.dto.UserDto;
import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.repository.NotificationRepository;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import com.ace_inspiration.team_joblify.service.hr_service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
        user.setDepartment(department);
        user.setCreatedDate(currentDate);
        user.setLastUpdatedDate(currentDate);
        user.setGender(Gender.valueOf(userDto.getGender()));

        userRepository.save(user);

        Notification notification = new Notification();

        User actionUser = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(notFound));
        notification.setMessage(userDto.getName() + " is created by " + actionUser.getName());
        notification.setTime(currentDate);
        notification.setLink("aaa");
        notificationRepository.save(notification);

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
        User user = userRepository.findByEmailAndIdNot(email, userId).orElse(null);

        return user != null;
    }

    @Override
    public boolean checkOldPassword(String password, long userId) {
        User user = userRepository.findById(userId).orElse(null);
        assert user != null;

        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public boolean passwordChange(String newPassword, long id){
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not Found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }
}
