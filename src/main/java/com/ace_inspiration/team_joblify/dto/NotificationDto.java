package com.ace_inspiration.team_joblify.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {

    private long id;
    private String message;
    private String link;
    private boolean isSeen;
    private boolean isDeleted;
    private long userId;
    private String username;
    private LocalDateTime time;

}
