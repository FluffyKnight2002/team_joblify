package com.ace_inspiration.team_joblify.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 30, nullable = false, unique = true)
    private String username;

    @Column(length = 30, nullable = false, unique = true)
    private String name;

    @Column(length = 30, nullable = false, unique = true)
    private String email;

    @Column(length = 15, nullable = false)
    private String phone;

    @Column(nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(columnDefinition = "longtext", nullable = false)
    private String address;

    @Lob
    @Column(columnDefinition = "longblob", nullable = false)
    private String photo;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime createdDate;

    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime lastUpdatedDate;

    @Column(columnDefinition = "longtext")
    private String note;

    @OneToMany(mappedBy = "createdUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Vacancy> createdVacancies = new ArrayList<>();

    @OneToMany(mappedBy = "updatedUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<VacancyInfo> updatedJobPosts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    @JsonIgnoreProperties("user")
    private Department department;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<NotificationUser> notificationUsers= new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Otp> otps= new ArrayList<>();

}
