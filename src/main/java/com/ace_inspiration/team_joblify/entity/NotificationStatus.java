package com.ace_inspiration.team_joblify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class NotificationStatus implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean makeAsRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id")
//    @JsonIgnoreProperties(value={"hibernateLazyInitializer","notificationStatuses"})
    @JsonManagedReference
    private Notification notification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
//    @JsonIgnoreProperties(value={"hibernateLazyInitializer","notificationStatuses"})
    @JsonBackReference
    private User user;
}
