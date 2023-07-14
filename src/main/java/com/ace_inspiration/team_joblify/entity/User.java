package com.ace_inspiration.team_joblify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(length = 15, nullable = false, unique = true)
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

    @Column(length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime lastUpdatedDate;

    @Column(columnDefinition = "longtext")
    private String note;

    @OneToMany(mappedBy = "createdUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Vacancy> createdVacancies =new ArrayList<>();

    @OneToMany(mappedBy = "updatedUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<VacancyDepartment> updatedJobPosts=new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Action> action=new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id")
    private Department department;
}
