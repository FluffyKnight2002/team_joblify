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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
<<<<<<< HEAD:src/main/java/com/ace_inspiration/team_joblify/entity/VacancyDepartment.java
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class VacancyDepartment implements Serializable {
=======
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class VacancyInfo implements Serializable {
>>>>>>> backup:src/main/java/com/ace_inspiration/team_joblify/entity/VacancyInfo.java

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
    @Enumerated(EnumType.STRING)
    private Level lvl;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private OnSiteOrRemote onSiteOrRemote;

    @Column(columnDefinition = "longtext")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updatedUser_id")
//    @JsonIgnoreProperties(value={"hibernateLazyInitializer","updatedJobPosts"})
    @JsonManagedReference
    private User updatedUser;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "vacancy_id")
//    @JsonIgnoreProperties(value={"hibernateLazyInitializer","vacancyDepartment"})
    @JsonManagedReference
    private Vacancy vacancy;

<<<<<<< HEAD:src/main/java/com/ace_inspiration/team_joblify/entity/VacancyDepartment.java
    @OneToMany(mappedBy = "vacancyDepartment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
=======
    @OneToMany(mappedBy = "vacancyInfo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
>>>>>>> backup:src/main/java/com/ace_inspiration/team_joblify/entity/VacancyInfo.java
//    @JsonIgnoreProperties(value={"hibernateLazyInitializer","vacancyDepartment"})
    @JsonBackReference
    private List<Candidate> candidate=new ArrayList<>();
}
