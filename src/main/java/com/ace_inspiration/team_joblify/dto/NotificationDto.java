package com.ace_inspiration.team_joblify.dto;

import com.ace_inspiration.team_joblify.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
public class NotificationDto {

    private long id;
    private String name;
    private String link;
    private boolean makeAsRead;
    private User user;
    private LocalDateTime time;

}
