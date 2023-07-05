package com.ace_inspiration.team_joblify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobPost implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private int requiredVacancies;

    @Column(nullable = false)
    private int hiredVacancies;

    @Column(columnDefinition ="longtext", nullable = false)
    private String description;

    @Column(columnDefinition ="longtext", nullable = false)
    private String requirements;

    @Column(columnDefinition ="longtext", nullable = false)
    private String responsibilities;

    @Column(columnDefinition ="longtext", nullable = false)
    private String preferences;

    @Column(length = 20, nullable = false)
    private String workingDay;

    @Column(length = 15, nullable = false)
    private String workingHour;

    @Column(length = 75, nullable = false)
    private String jobLocation;

    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Column(nullable = false)
    private Date openDate;

    @Column(nullable = false)
    private Date closeDate;

    @Column(length = 24, nullable = false)
    private String salary;

    @Column(nullable = false)
    private Date createdDate;

    @Column(nullable = false)
    private Date updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdUser_id")
    private User createdUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updatedUser_id")
    private User updatedUser;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Applicant> applicants=new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;
}
