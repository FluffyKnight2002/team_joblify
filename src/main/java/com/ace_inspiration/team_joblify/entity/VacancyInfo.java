package com.ace_inspiration.team_joblify.entity;

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
@Table(name = "vacancy_info")
public class VacancyInfo implements Serializable {

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Column(nullable = false)
    private LocalDate openDate;

    @Column(nullable = false)
    private LocalDate closeDate;

    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime updatedTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Level lvl;

    @Column( nullable = false)
    @Enumerated(EnumType.STRING)
    private OnSiteOrRemote onSiteOrRemote;

    @Column(columnDefinition = "longtext")
    private String note;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_user_id")
    private User updatedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacancy_id")
    private Vacancy vacancy;

    @OneToMany(mappedBy = "vacancyInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Candidate> candidate=new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;


}
