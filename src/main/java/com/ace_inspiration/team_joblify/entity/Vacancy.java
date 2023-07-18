package com.ace_inspiration.team_joblify.entity;

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
public class Vacancy implements Serializable{

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

    @Column(length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private Level lvl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdUser_id")
    private User createdUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    @OneToMany(mappedBy = "vacancy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<VacancyDepartment> vacancyDepartment =new ArrayList<>();

    @OneToMany(mappedBy = "vacancy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Candidate> candidate=new ArrayList<>();

}
