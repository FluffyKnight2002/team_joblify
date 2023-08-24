package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.dto.UserDto;
import com.ace_inspiration.team_joblify.entity.Department;
import com.ace_inspiration.team_joblify.entity.User;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.repository.NotificationRepository;
import com.ace_inspiration.team_joblify.repository.NotificationUserRepository;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplementTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationUserRepository notificationUserRepository;

    private UserServiceImplement implement;
    @BeforeEach
    void setUp() {
        implement = new UserServiceImplement(passwordEncoder, userRepository, departmentRepository);

    }

    @Test
    void userCreate() {

        LocalDateTime currentDate = LocalDateTime.now();

        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
        userDto.setPhone("1234567890");
        userDto.setAddress("Test Address");
        userDto.setRole("DEFAULT_HR");
        userDto.setNote("Test Note");
        userDto.setDepartment("Test Department");
        userDto.setGender("MALE");

        when(departmentRepository.findByName(userDto.getDepartment())).thenReturn(null);
        when(departmentRepository.save(any(Department.class))).thenReturn(Department.builder().id(1L).name(userDto.getDepartment()).build());

        when(userRepository.save(any(User.class))).thenReturn(null);

        verify(departmentRepository, times(1)).findByName(userDto.getDepartment());
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void findById() {
    }

    @Test
    void savePassword() {
    }

    @Test
    void adminProfileEdit() {
    }

    @Test
    void userProfileEdit() {
    }

    @Test
    void emailDuplication() {
    }

    @Test
    void emailDuplicationExceptMine() {
    }

    @Test
    void checkOldPassword() {
    }

    @Test
    void passwordChange() {
    }

    @Test
    void suspend() {
    }

    @Test
    void activate() {
    }

    @Test
    void findByEmail() {
    }
}