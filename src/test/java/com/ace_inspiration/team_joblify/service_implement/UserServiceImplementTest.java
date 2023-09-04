package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.dto.UserDto;
import com.ace_inspiration.team_joblify.entity.Department;
import com.ace_inspiration.team_joblify.entity.Role;
import com.ace_inspiration.team_joblify.entity.User;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.repository.NotificationRepository;
import com.ace_inspiration.team_joblify.repository.NotificationUserRepository;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

//    @BeforeEach
//    void setUp() {
//
//        implement = new UserServiceImplement(passwordEncoder, userRepository, departmentRepository, resourceLoader);
//
//    }


    @ParameterizedTest
    @MethodSource("initializeDepartmentParametersForUserCreate")
    void userCreate(Department department, String departmentName, MultipartFile photo) throws IOException {

        LocalDateTime currentDate = LocalDateTime.now();

        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
        userDto.setPhone("1234567890");
        userDto.setAddress("Test Address");
        userDto.setRole(Role.SENIOR_HR.name());

        userDto.setPhoto(photo);
        userDto.setNote("Test Note");
        userDto.setDepartment(departmentName);
        userDto.setGender("MALE");

        if (Objects.equals(departmentName, "Management")) {
            // Mock behavior of DepartmentRepository
            when(departmentRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
            when(departmentRepository.save(any(Department.class))).thenReturn(department);

        } else if (Objects.equals(departmentName, "Human Resources")) {
            // Mock behavior of DepartmentRepository
            when(departmentRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.ofNullable(department));
        }

        // Mock behavior of UserRepository
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // Call the method to be tested
        implement.userCreate(userDto, 1L);

        // Verify interactions
        verify(departmentRepository, times(1)).findByNameIgnoreCase(anyString());
        verify(departmentRepository, times(Objects.equals(departmentName, "Human Resources") ? 0 : 1)).save(any(Department.class));
        verify(userRepository, times(1)).save(any(User.class));
    }



    static Stream<Arguments> initializeDepartmentParametersForUserCreate() {
        Department d = Department.builder()
                .name("Human Resources")
                .build();

        MultipartFile photo = new MockMultipartFile(
                "photo.jpg", // Original filename
                "photo.jpg", // Content type
                "image/jpeg", // Content type
                new byte[1024] // Photo content as byte array
        );


        return Stream.of(
                Arguments.of(d, "Human Resources", photo),
                Arguments.of(d, "Management", null),
                Arguments.of(d, "Human Resources", null),
                Arguments.of(d, "Management", photo)
        );

    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    void findById(long id) {

        if (id == 1L) {
            when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(any(User.class)));
        } else if (id == 2L) {
            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        }

        implement.findById(id);
        verify(userRepository, times(1)).findById(id);

    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    void savePassword(long id) {
        if (id == 1L) {
            User user = new User();
            user.setPassword("111111");

            when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
            when(userRepository.save(user)).thenReturn(null);
        } else if (id == 2L) {
            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        }

        implement.savePassword("1122121", id);

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(id == 1L ? 1 : 0)).save(any(User.class));
    }

    @ParameterizedTest
    @MethodSource("initializeDepartmentParametersForAdminProfileUpdate")
    void adminProfileEdit(Department department, String departmentName, MultipartFile photo, String email) throws IOException {

        LocalDateTime currentDate = LocalDateTime.now();

        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
        userDto.setPhone("1234567890");
        userDto.setAddress("Test Address");
        userDto.setRole(Role.SENIOR_HR.name());


        userDto.setPhoto(photo);
        userDto.setNote("Test Note");
        userDto.setDepartment(departmentName);
        userDto.setGender("MALE");

        User user = new User();
        user.setName("Admin");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        if(Objects.equals(email, "ace1122121@gmail.com")) {
            if (Objects.equals(departmentName, "Management")) {
                // Mock behavior of DepartmentRepository
                when(departmentRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

                when(departmentRepository.save(any(Department.class))).thenReturn(department);

            } else if (Objects.equals(departmentName, "Human Resources")) {
                // Mock behavior of DepartmentRepository
                when(departmentRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.ofNullable(department));
            }

            // Mock behavior of UserRepository
            when(userRepository.save(any(User.class))).thenReturn(new User());
        } else if (Objects.equals(email, "ak4312040@gmail.com")) {
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        }
        // Call the method to be tested
        implement.adminProfileEdit(userDto, "ace1122121@gmail.com");

        // Verify interactions

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(departmentRepository, times(Objects.equals(email, "ace1122121@gmail.com") ? 1 : 0)).findByNameIgnoreCase(anyString());

        if (Objects.equals(email, "ace1122121@gmail.com") && Objects.equals(departmentName, "Management")) {
            verify(departmentRepository, times(1)).save(any(Department.class));
        }
        verify(userRepository, times(Objects.equals(email, "ace1122121@gmail.com") ? 1 : 0)).save(any(User.class));
    }

    static Stream<Arguments> initializeDepartmentParametersForAdminProfileUpdate() {
        Department d = Department.builder()
                .name("Human Resources")
                .build();

        MultipartFile photo = new MockMultipartFile(
                "photo.jpg", // Original filename
                "photo.jpg", // Content type
                "image/jpeg", // Content type
                new byte[1024] // Photo content as byte array
        );


        return Stream.of(
                Arguments.of(d, "Human Resources", photo, "ace1122121@gmail.com"),
                Arguments.of(d, "Human Resources", null, "ace1122121@gmail.com"),
                Arguments.of(d, "Human Resources", photo, "ak4312040@gmail.com"),
                Arguments.of(d, "Human Resources", null, "ak4312040@gmail.com"),

                Arguments.of(d, "Management", null, "ace1122121@gmail.com"),
                Arguments.of(d, "Management", photo, "ace1122121@gmail.com"),
                Arguments.of(d, "Management", null, "ak4312040@gmail.com"),
                Arguments.of(d, "Management", photo, "ak4312040@gmail.com")
        );

    }


    @ParameterizedTest
    @MethodSource("initializeDepartmentParametersForAdminProfileUpdate")
    void userProfileEdit(Department department, String departmentName, MultipartFile photo, String email) throws IOException {

        LocalDateTime currentDate = LocalDateTime.now();

        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
        userDto.setPhone("1234567890");
        userDto.setAddress("Test Address");
        userDto.setRole(Role.SENIOR_HR.name());


        userDto.setPhoto(photo);
        userDto.setNote("Test Note");
        userDto.setDepartment(departmentName);
        userDto.setGender("MALE");

        User user = new User();
        user.setName("Admin");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        if(Objects.equals(email, "ace1122121@gmail.com")) {
            // Mock behavior of UserRepository
            when(userRepository.save(any(User.class))).thenReturn(new User());

        } else if (Objects.equals(email, "ak4312040@gmail.com")) {
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        }
        // Call the method to be tested
        implement.userProfileEdit(userDto, "ace1122121@gmail.com");

        // Verify interactions

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(Objects.equals(email, "ace1122121@gmail.com") ? 1 : 0)).save(any(User.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ak4312040@gmail.com", "ace1122121@gmail.com"})
    void emailDuplication(String email) {
        if(email.equals("ak4312040@gmail.com")){
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        } else if (email.equals("ace1122121@gmail.com")) {
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(any(User.class)));
        }
        implement.emailDuplication(email);
        verify(userRepository, times(1)).findByEmail(anyString());

    }

    @ParameterizedTest
    @ValueSource(strings = {"ak4312040@gmail.com", "ace1122121@gmail.com"})
    void checkOldPassword(String email) {

        if(email.equals("ak4312040@gmail.com")){
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        } else if (email.equals("ace1122121@gmail.com")) {
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(any(User.class)));
        }
        implement.emailDuplication(email);
        verify(userRepository, times(1)).findByEmail(anyString());

    }

    @ParameterizedTest
    @MethodSource("passwordAndEmailForCheckPassword")
    void checkOldPassword(String email, String password) {

        if (email.equals("ak4312040@gmail.com")) {
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        } else if (email.equals("ace@gmail.com")) {
            User user = new User();
            user.setPassword("ace1122121"); // Set the password to a hashed value for testing
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        }

        boolean result = implement.checkOldPassword(password, email);

        verify(userRepository, times(1)).findByEmail(anyString());

        if (email.equals("ace@gmail.com")) {
            assertTrue(result); // Expecting true if the email matches and password is correct
        } else {
            assertFalse(result); // Expecting false for all other cases
        }
    }

    static Stream<Arguments> passwordAndEmailForCheckPassword() {

        return Stream.of(
                Arguments.of("ak4312040@gmail.com", "ace1122121"),
                Arguments.of("ak4312040@gmail.com", "1122121"),
                Arguments.of("ace@gmail.com", "ace1122121"),
                Arguments.of("ace@gmail.com", "1122121")
        );

    }

    @ParameterizedTest
    @MethodSource("passwordAndEmailForPasswordChange")
    void passwordChange(String email, String password) {

        if (email.equals("ak4312040@gmail.com")) {
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        } else if (email.equals("ace@gmail.com")) {
            User user = new User();
            user.setPassword("ace1122121"); // Set the password to a hashed value for testing
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        }

        boolean result = implement.passwordChange(password, email);

        verify(userRepository, times(1)).findByEmail(anyString());

        if (email.equals("ace@gmail.com")) {
            assertTrue(result); // Expecting true if the email matches and password is correct
        } else {
            assertFalse(result); // Expecting false for all other cases
        }

    }


    static Stream<Arguments> passwordAndEmailForPasswordChange() {

        return Stream.of(
                Arguments.of("ak4312040@gmail.com", "1122121"),
                Arguments.of("ace@gmail.com", "1122121")
        );

    }


    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    void suspend(long id) {

        if (id == 2L) {
            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        } else if (id == 1L) {
            User user = new User();
            user.setAccountStatus(false);
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(any(User.class));
        }

        boolean result = implement.suspend(id);

        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(id == 1L ? 1 : 0)).save(any(User.class)); // Replace 'user' with 'any(User.class)'

        if (id == 2L) {
            assertFalse(result); // Expecting false if the user with id 2 is not found
        } else {
            assertTrue(result); // Expecting true for all other cases
        }
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    void activate(long id) {

        if (id == 2L) {
            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        } else if (id == 1L) {
            User user = new User();
            user.setAccountStatus(true);
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(any(User.class));
        }

        boolean result = implement.activate(id);

        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(id == 1L ? 1 : 0)).save(any(User.class)); // Replace 'user' with 'any(User.class)'

        if (id == 2L) {
            assertFalse(result); // Expecting false if the user with id 2 is not found
        } else {
            assertTrue(result); // Expecting true for all other cases
        }

    }

    @ParameterizedTest
    @ValueSource(strings = {"ace@gmail.com", "ak4312040@gmail.com"})
    void findByEmail(String email) {

        if (email.equals("ak4312040@gmail.com")) {
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        } else if (email.equals("ace@gmail.com")) {
            User user = new User();
            user.setPassword("ace1122121"); // Set the password to a hashed value for testing
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        }

        implement.findByEmail(email);

        verify(userRepository, times(1)).findByEmail(anyString());

    }

    @ParameterizedTest
    @ValueSource(strings = {"09777159555", "09773137253"})
    void checkPhoneDuplicate(String phone) {

        if (phone.equals("09773137253")) {
            when(userRepository.findByPhone(anyString())).thenReturn(Optional.empty());
        } else if (phone.equals("09777159555")) {
            User user = new User();
            user.setPhone("777159555"); // Set the password to a hashed value for testing
            when(userRepository.findByPhone(anyString())).thenReturn(Optional.of(user));
        }

        implement.checkPhoneDuplicate(phone);

        verify(userRepository, times(1)).findByPhone(anyString());



    }

    @ParameterizedTest
    @ValueSource(strings = {"Admin ", "CZe"})
    void checkUsernameDuplicate(String username) {

        if (username.equals("09773137253")) {
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        } else if (username.equals("09777159555")) {
            User user = new User();
            user.setPhone("AKZ"); // Set the password to a hashed value for testing
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        }

        implement.checkUsernameDuplicate(username);

        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @ParameterizedTest
    @MethodSource("emailAndIdProvider")
    void emailDuplicationExceptHimself(String email, long id) {
        if (email.equals("ace@gmail.com") && id != 1L) {
            User user = new User();
            user.setEmail("ace@gmail.com"); // Set the password to a hashed value for testing
            when(userRepository.findByEmailAndIdNot(anyString(), anyLong())).thenReturn(Optional.of(user));


        } else {
            when(userRepository.findByEmailAndIdNot(anyString(), anyLong())).thenReturn(Optional.empty());
        }

        boolean result = implement.emailDuplicationExceptHimself(email, id);

        verify(userRepository, times(1)).findByEmailAndIdNot(anyString(), anyLong());

        if(email.equals("ace@gmail.com") && id != 1L) {
            assertTrue(result);
        } else {
            assertFalse(result);
        }
    }

    static Stream<Arguments> emailAndIdProvider() {
        return Stream.of(
                Arguments.of("ace@gmail.com", 1L),
                Arguments.of("ak4312040@gmail.com", 1L),
                Arguments.of("ace@gmail.com", 2L),
                Arguments.of("ak4312040@gmail.com", 2L)
        );
    }

    @ParameterizedTest
    @MethodSource("phoneAndIdProvider")
    void checkPhoneDuplicateExceptHimself(String phone, long id) {
        if (phone.equals("09777159555") && id != 1L) {
            User user = new User();
            user.setEmail("09777159555"); // Set the password to a hashed value for testing
            when(userRepository.findByPhoneAndIdNot(anyString(), anyLong())).thenReturn(Optional.of(user));


        } else {
            when(userRepository.findByPhoneAndIdNot(anyString(), anyLong())).thenReturn(Optional.empty());
        }

        boolean result = implement.checkPhoneDuplicateExceptHimself(phone, id);

        verify(userRepository, times(1)).findByPhoneAndIdNot(anyString(), anyLong());

        if(phone.equals("09777159555") && id != 1L) {
            assertTrue(result);
        } else {
            assertFalse(result);
        }
    }

    static Stream<Arguments> phoneAndIdProvider() {
        return Stream.of(
                Arguments.of("09777159555", 1L),
                Arguments.of("09773137253", 1L),
                Arguments.of("09777159555", 2L),
                Arguments.of("09773137253", 2L)
        );
    }
}