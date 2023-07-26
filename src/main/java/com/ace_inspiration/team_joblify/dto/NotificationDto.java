package com.ace_inspiration.team_joblify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {

    private long id;
    private String message;
    private String link;
    private boolean isSeen;
    private long userId;
    private String username;
    private LocalDateTime time;

}
