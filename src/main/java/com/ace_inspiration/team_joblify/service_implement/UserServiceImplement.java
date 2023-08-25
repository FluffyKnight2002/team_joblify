package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.config.ProfileGenerator;
import com.ace_inspiration.team_joblify.dto.UserDto;
import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.repository.NotificationRepository;
import com.ace_inspiration.team_joblify.repository.NotificationUserRepository;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import com.ace_inspiration.team_joblify.service.hr_service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    @Value("${app.default.user.password}")
    private String password;

    @Override
    public User userCreate(UserDto userDto, long userId) throws IOException {
        
        byte[] imageBytes;

        if(userDto.getPhoto().isEmpty()){
            imageBytes = ProfileGenerator.generateAvatar(userDto.getUsername());

        } else {
            imageBytes = userDto.getPhoto().getBytes();
        }

        Optional<Department> departmentOptional = departmentRepository.findByNameIgnoreCase(userDto.getDepartment());

        Department department;

        if (departmentOptional.isPresent()) {
            department = departmentOptional.get();
        } else {
            Department newDepartment = new Department();
            newDepartment.setName(userDto.getDepartment());
            department = departmentRepository.save(newDepartment);
        }

        LocalDateTime currentDate = LocalDateTime.now();

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());
        user.setPhoto(Base64.getEncoder().encodeToString(imageBytes));
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.valueOf(userDto.getRole()));
        user.setNote(userDto.getNote());
        user.setAccountStatus(true);
        user.setDepartment(department);
        user.setCreatedDate(currentDate);
        user.setLastUpdatedDate(currentDate);
        user.setGender(Gender.valueOf(userDto.getGender()));

        return userRepository.save(user);
    }

    @Override
    public User findById(long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public void savePassword(String password, long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }
    }

    @Override
    public User adminProfileEdit(UserDto userDto, String email) throws IOException {
        LocalDateTime currentDate = LocalDateTime.now();
        byte[] imageBytes;
        if(userDto.getPhoto().isEmpty()){
            imageBytes = ProfileGenerator.generateAvatar(userDto.getUsername());

        } else {
            imageBytes = userDto.getPhoto().getBytes();
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Optional<Department> departmentOptional = departmentRepository.findByNameIgnoreCase(userDto.getDepartment());
            Department department;

            if (departmentOptional.isPresent()) {
                department = departmentOptional.get();
            } else {
                Department newDepartment = new Department();
                newDepartment.setName(userDto.getDepartment());
                department = departmentRepository.save(newDepartment);
            }

            user.setUsername(userDto.getUsername());
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setPhone(userDto.getPhone());
            user.setAddress(userDto.getAddress());
            user.setPhoto(Base64.getEncoder().encodeToString(imageBytes));
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(Role.valueOf(userDto.getRole()));
            user.setNote(userDto.getNote());
            user.setDepartment(department);
            user.setLastUpdatedDate(currentDate);
            user.setGender(Gender.valueOf(userDto.getGender()));

            return userRepository.save(user);
        }
        return null;
    }


    @Override
    public User userProfileEdit(UserDto userDto, String email) throws IOException {

        LocalDateTime currentDate = LocalDateTime.now();
        byte[] imageBytes;

        if(userDto.getPhoto().isEmpty()){
            imageBytes = ProfileGenerator.generateAvatar(userDto.getUsername());

        } else {
            imageBytes = userDto.getPhoto().getBytes();
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            
            user.setUsername(userDto.getUsername());
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setPhone(userDto.getPhone());
            user.setAddress(userDto.getAddress());
            user.setPhoto(Base64.getEncoder().encodeToString(imageBytes));
            user.setNote(userDto.getNote());
            user.setLastUpdatedDate(currentDate);
            user.setGender(Gender.valueOf(userDto.getGender()));

            return userRepository.save(user);
        }
        return null;
    }
    @Override
    public boolean emailDuplication(String email) {
        User user = userRepository.findByEmail(email).orElse(null);

        return user != null;
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
        User user = userRepository.findById(id).orElseThrow(null);
        if(user != null) {
            user.setAccountStatus(false);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean activate(long id) {
        User user = userRepository.findById(id).orElseThrow(null);
        if(user != null) {
            user.setAccountStatus(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public boolean checkPhoneDuplicate(String phone) {
        User user = userRepository.findByPhone(phone).orElse(null);
        return user != null;
    }

    @Override
    public boolean checkUsernameDuplicate(String username) {
       User user = userRepository.findByUsername(username).orElse(null);
        return user != null;
    }

    @Override
    public boolean emailDuplicationExceptHimself(String email, long userId) {
        User user = userRepository.findByEmailAndIdNot(email, userId).orElse(null);

        return user != null;
    }

    @Override
    public boolean checkPhoneDuplicateExceptHimself(String phone, long userId) {
        User user = userRepository.findByPhoneAndIdNot(phone, userId).orElse(null);
        return user != null;
    }

    @Override
    public boolean checkUsernameDuplicateExceptHimself(String username, long userId) {
       User user = userRepository.findByUsernameAndIdNot(username, userId).orElse(null);

        return user != null;
    }
}
