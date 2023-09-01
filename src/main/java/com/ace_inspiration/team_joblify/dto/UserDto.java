package com.ace_inspiration.team_joblify.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private MultipartFile photo;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    private boolean accountStatus;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone cannot be blank")
    @Pattern(regexp = "\\d{11,13}", message = "Invalid phone number format. It should be an 11 to 13-digit number.")
    private String phone;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotBlank(message = "Gender cannot be blank")
    private String gender;

    @NotBlank(message = "Role cannot be blank")
    private String role;

    @NotBlank(message = "Note cannot be blank")
    private String note;

    @NotBlank(message = "Department cannot be blank")
    private String department;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password should have at least 8 characters")
    private String password;
}
