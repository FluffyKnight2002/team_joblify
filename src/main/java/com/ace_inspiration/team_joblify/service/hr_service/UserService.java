package com.ace_inspiration.team_joblify.service.hr_service;

import com.ace_inspiration.team_joblify.dto.UserDto;
import com.ace_inspiration.team_joblify.entity.User;

import java.io.IOException;

public interface UserService {
    User userCreate(UserDto userDto, long userId) throws IOException;

    User findById(long userId);

    void savePassword(String password, long userId);

    User adminProfileEdit(UserDto userDto, String email) throws IOException;

    User userProfileEdit(UserDto userDto, String email) throws IOException;

    boolean checkOldPassword(String password, String email);

    boolean passwordChange(String newPassword, String email);

    boolean suspend(long id);

    boolean activate(long id);

    User findByEmail(String email);

    boolean emailDuplication(String email);

    boolean checkPhoneDuplicate(String phone);

    boolean checkUsernameDuplicate(String username);

    boolean emailDuplicationExceptHimself(String email, long userId);

    boolean checkPhoneDuplicateExceptHimself(String phone, long userId);

    boolean checkUsernameDuplicateExceptHimself(String username, long userId);

}
