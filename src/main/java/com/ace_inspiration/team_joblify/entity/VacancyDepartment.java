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
public class VacancyDepartment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, columnDefinition = "decimal(10,2)")
    private double salary;

    @Column(nullable = false)
    private int post;

    @Column(nullable = false)
    private int hiredPost;

    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private VacancyType jobType;

    @Column(length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private Level lvl;

    @Column(nullable = false)
    private LocalDate openDate;

    @Column(nullable = false)
    private LocalDate closeDate;

    @Column(nullable = false)
    private LocalDateTime updatedTime;

    @Column(columnDefinition = "longtext")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updatedUser_id")
    private User updatedUser;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "vacancy_id")
    private Vacancy vacancy;

    @OneToMany(mappedBy = "vacancyDepartment",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();
}
