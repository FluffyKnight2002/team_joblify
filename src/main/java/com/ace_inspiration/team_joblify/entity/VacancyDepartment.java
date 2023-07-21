package com.ace_inspiration.team_joblify.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VacancyDepartment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition ="longtext", nullable = false)
    private String description;

    @Column(columnDefinition ="longtext", nullable = false)
    private String requirements;

    @Column(columnDefinition ="longtext", nullable = false)
    private String responsibilities;

    @Column(columnDefinition ="longtext", nullable = false)
    private String preferences;

    @Column(length = 30, nullable = false)
    private String workingDays;

    @Column(length = 30, nullable = false)
    private String workingHours;

    @Column(length = 24, nullable = false)
    private String salary;

    @Column(nullable = false)
    private int post;

    @Column(nullable = false)
    private int hiredPost;

    @Column(length = 10, nullable = false)
    private String jobType;

    @Column(nullable = false)
    private LocalDate openDate;

    @Column(nullable = false)
    private LocalDate closeDate;

    @Column(nullable = false)
    private LocalDateTime updatedTime;

    @Column(nullable = false, length = 30)
    private Level lvl;

    @Column(nullable = false, length = 30)
    private OnSiteOrRemote onSiteOrRemote;

    @Column(columnDefinition = "longtext")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updatedUser_id")
    @JsonIdentityReference(alwaysAsId = true)
    private User updatedUser;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "vacancy_id")
    private Vacancy vacancy;

    @OneToMany(mappedBy = "vacancyDepartment",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "vacancyDepartment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Candidate> candidate=new ArrayList<>();
}
