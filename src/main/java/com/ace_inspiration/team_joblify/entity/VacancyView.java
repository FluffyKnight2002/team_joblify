package com.ace_inspiration.team_joblify.entity;

import com.querydsl.core.annotations.Immutable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Immutable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vacancy_view")
public class VacancyView implements Serializable {

    @Id
    private long id;

    @Column(name = "close_date")
    private LocalDate closeDate;

    @Column(name = "description")
    private String description;

    @Column(name = "hired_post")
    private int hiredPost;

    @Column(name = "job_type")
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Column(name = "level")
    @Enumerated(EnumType.STRING)
    private Level level;

    @Column(name = "note")
    private String note;

    @Column(name = "on_site_or_remote")
    @Enumerated(EnumType.STRING)
    private OnSiteOrRemote onSiteOrRemote;

    @Column(name = "open_date")
    private LocalDate openDate;

    @Column(name = "required_post")
    private int post;

    @Column(name = "preferences")
    private String preferences;

    @Column(name = "requirements")
    private String requirements;

    @Column(name = "responsibilities")
    private String responsibilities;

    @Column(name = "salary")
    private double salary;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @Column(name = "working_hours")
    private String workingHours;

    @Column(name = "working_days")
    private String workingDays;

    @Column(name = "applicants")
    private long applicants;

    @Column(name = "vacancy_id")
    private long vacancyId;

    @Column(name = "created_date")
    private LocalDateTime createdTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "position")
    private String position;

    @Column(name = "department")
    private String department;

    @Column(name = "address")
    private String address;

    @Column(name = "created_username")
    private String createdUsername;

    @Column(name = "updated_username")
    private String updatedUsername;


}
