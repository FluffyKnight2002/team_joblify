package com.ace_inspiration.team_joblify.dto;

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

    private String username;

    private String name;

    private String email;

    private String phone;

    private String address;

    private int gender;

    private MultipartFile photo;

    private String password;

    private String role;

    private String note;

    private long department;
}
