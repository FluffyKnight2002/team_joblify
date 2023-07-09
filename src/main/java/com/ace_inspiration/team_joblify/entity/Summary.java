package com.ace_inspiration.team_joblify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Summary implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private Date dob;

    @Column(length = 8 ,nullable = false)
    private Gender gender;

    @Column(length = 15, nullable = false)
    private String phone;

    @Column(length = 30, nullable = false)
    private String email;

    @Column(nullable = false)
    private String education;

    @Column(nullable = false)
    private String techSkills;

    @Column(nullable = false)
    private String languages;

    @Column(nullable = false, length = 100)
    private String applyPosition;

    @Column(length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    private Level lvl;

    @Column(nullable = false, length = 25)
    private String specialistTech;

    @Column(nullable = false, length = 25)
    private String experience;

    @Column(nullable = false)
    private int expectedSalary;

    @OneToOne(mappedBy = "summary", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Candidate candidate;

    @OneToMany(mappedBy = "summary", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Skills> skills;
}
