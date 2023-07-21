package com.ace_inspiration.team_joblify.dto;

import com.ace_inspiration.team_joblify.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {

    private long id;
    private String message;
    private String link;
    private boolean makeAsRead;
    private User user;
    private LocalDateTime time;

}
