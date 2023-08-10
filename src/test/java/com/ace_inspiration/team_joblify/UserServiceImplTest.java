//package com.ace_inspiration.team_joblify;
//
//import com.ace_inspiration.team_joblify.entity.Gender;
//import com.ace_inspiration.team_joblify.entity.Role;
//import com.ace_inspiration.team_joblify.entity.User;
//
//import java.time.LocalDateTime;
//
//public class UserServiceImplTest {
//        User user = Mockito.mock(User.class);
//
//        // Set mock values for the User fields
//        when(user.getId()).thenReturn(1L);
//        when(user.getUsername()).thenReturn("mockUsername");
//        when(user.getName()).thenReturn("mockName");
//        when(user.getEmail()).thenReturn("mockEmail");
//        when(user.getPhone()).thenReturn("mockPhone");
//        when(user.getGender()).thenReturn(Gender.MALE);
//        when(user.getAddress()).thenReturn("mockAddress");
//        when(user.getPhoto()).thenReturn("mockPhoto");
//        when(user.getPassword()).thenReturn("mockPassword");
//        when(user.getRole()).thenReturn(Role.USER);
//        when(user.getCreatedDate()).thenReturn(LocalDateTime.now());
//        when(user.getLastUpdatedDate()).thenReturn(LocalDateTime.now());
//        when(user.getNote()).thenReturn("mockNote");
//        when(user.getDepartment()).thenReturn(null); // Set department mock separately
//
//        return user;
//    }
//}
