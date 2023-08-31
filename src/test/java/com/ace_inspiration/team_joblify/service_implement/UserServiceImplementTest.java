package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.config.ProfileGenerator;
import com.ace_inspiration.team_joblify.dto.UserDto;
import com.ace_inspiration.team_joblify.entity.Department;
import com.ace_inspiration.team_joblify.entity.Role;
import com.ace_inspiration.team_joblify.entity.User;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.repository.NotificationRepository;
import com.ace_inspiration.team_joblify.repository.NotificationUserRepository;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

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
    private ResourceLoader resourceLoader;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationUserRepository notificationUserRepository;

    private UserServiceImplement implement;
    Department d;

    @BeforeEach
    void setUp() {


        implement = new UserServiceImplement(passwordEncoder, userRepository, departmentRepository, resourceLoader);

    }


    @ParameterizedTest
    @ValueSource(strings = {"Human Resources", "Management"})
    void userCreate(String department) throws IOException {

        LocalDateTime currentDate = LocalDateTime.now();

        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
        userDto.setPhone("1234567890");
        userDto.setAddress("Test Address");
        userDto.setRole(Role.SENIOR_HR.name());

        MultipartFile photo = new MockMultipartFile(
                "photo.jpg",      // Original filename
                "photo.jpg",      // Content type
                "image/jpeg",     // Content type
                new byte[1024]    // Photo content as byte array
        );
        userDto.setPhoto(photo);
        userDto.setNote("Test Note");
        userDto.setDepartment(department);
        userDto.setGender("MALE");

        // Mock behavior of DepartmentRepository
        when(departmentRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());


        // Mock behavior of UserRepository
        when(userRepository.save(any(User.class))).thenReturn(new User());



        // Call the method to be tested
        implement.userCreate(userDto, 1L);

        // Verify interactions
        verify(departmentRepository, times(1)).save(any(Department.class));
        verify(userRepository, times(1)).save(any(User.class));
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