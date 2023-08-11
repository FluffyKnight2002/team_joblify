package com.ace_inspiration.team_joblify.service.hr_service;

import com.ace_inspiration.team_joblify.dto.UserDto;
import com.ace_inspiration.team_joblify.entity.User;

import java.io.IOException;
import java.util.Optional;

public interface UserService {
    User userCreate(UserDto userDto, long userId) throws IOException;
    Optional<User> findById(long userId);

    void savePassword(String password, long userId);

    User adminProfileEdit(UserDto userDto, long userId) throws IOException;

    User userProfileEdit(UserDto userDto, long userId) throws IOException;

    boolean emailDuplication(String email);

    boolean emailDuplicationExceptMine(String email, long userId);

    boolean checkOldPassword(String password, long userId);

    boolean passwordChange(String newPassword, long id);

}
