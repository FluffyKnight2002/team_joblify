package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.config.MyUserDetails;
import com.ace_inspiration.team_joblify.config.MyUserDetailsService;
import com.ace_inspiration.team_joblify.config.ProfileGenerator;
import com.ace_inspiration.team_joblify.dto.UserDto;
import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import com.ace_inspiration.team_joblify.service.hr_service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final ResourceLoader resourceLoader;
    private final SessionRegistry sessionRegistry;
    private final MyUserDetailsService myUserDetailsService;

    @Value("${app.default.user.password}")
    private String password;

    private final String location = "classpath:/static/assets/images/faces/5.jpg";

    @Override
    public User userCreate(UserDto userDto, long userId) throws IOException {

        byte[] imageBytes;

        if (userDto.getPhoto() == null || userDto.getPhoto().isEmpty()) {
            imageBytes = ProfileGenerator.generateAvatar(userDto.getUsername(), resourceLoader);

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
        if (userDto.getPhoto() == null || userDto.getPhoto().isEmpty()) {
            imageBytes = ProfileGenerator.generateAvatar(userDto.getUsername(), resourceLoader);

        } else {
            imageBytes = userDto.getPhoto().getBytes();
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Optional<Department> departmentOptional = departmentRepository
                    .findByNameIgnoreCase(userDto.getDepartment());
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

        if (userDto.getPhoto() == null || userDto.getPhoto().isEmpty()) {
            imageBytes = ProfileGenerator.generateAvatar(userDto.getUsername(), resourceLoader);
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
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            passwordEncoder.matches(password, user.get().getPassword());
            return true;
        }
        return false;
    }

    @Override
    public boolean passwordChange(String newPassword, String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            user.get().setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean suspend(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setAccountStatus(false);
            userRepository.save(user.get());

            List<Object> principals = sessionRegistry.getAllPrincipals();

            for (Object principal : principals) {
                if (principal instanceof MyUserDetails) {
                    MyUserDetails myUserDetails = (MyUserDetails) principal;
                    if (myUserDetails.getUsername().equals(user.get().getUsername())) {
                        List<SessionInformation> sessionInfoList = sessionRegistry.getAllSessions(myUserDetails, false);
        
                        for (SessionInformation sessionInformation : sessionInfoList) {
                            sessionInformation.expireNow(); // Invalidates the session
                        }
                    }
                }
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean activate(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setAccountStatus(true);
            userRepository.save(user.get());
            return true;
        }
        return false;
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    @Override
    public boolean checkPhoneDuplicate(String phone) {
        Optional<User> user = userRepository.findByPhone(phone);
        return user.isPresent();
    }

    @Override
    public boolean checkUsernameDuplicate(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent();
    }

    @Override
    public boolean emailDuplicationExceptHimself(String email, long userId) {
        Optional<User> user = userRepository.findByEmailAndIdNot(email, userId);
        return user.isPresent();
    }

    @Override
    public boolean checkPhoneDuplicateExceptHimself(String phone, long userId) {
        Optional<User> user = userRepository.findByPhoneAndIdNot(phone, userId);
        return user.isPresent();
    }

    @Override
    public boolean checkUsernameDuplicateExceptHimself(String username, long userId) {
        Optional<User> user = userRepository.findByUsernameAndIdNot(username, userId);
        return user.isPresent();
    }
}
