package com.ace_inspiration.team_joblify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "summary")
public class Summary implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(length = 8 ,nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length = 15, nullable = false)
    private String phone;

    @Column(length = 30, nullable = false)
    private String email;

    @Column(nullable = false)
    private String education;

    @Column(nullable = false, length = 100)
    private String applyPosition;

    @Column(length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    private Level lvl;

    @Column(nullable = false, length = 25)
    private String specialistTech;

    @Column(nullable = false, length = 25)
    private String experience;

    @Column(nullable = false, columnDefinition = "decimal(10,2)")
    private double expectedSalary;

    @OneToOne(mappedBy = "summary", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Candidate candidate;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "summary_language_skills", joinColumns = @JoinColumn(name = "summary_id"),
    inverseJoinColumns = @JoinColumn(name = "language_skills_id"))
    private List<LanguageSkills> languageSkills= new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "summary_tech_skills", joinColumns = @JoinColumn(name = "summary_id"),
            inverseJoinColumns = @JoinColumn(name = "tech_skills_id"))
    private List<TechSkills> techSkills= new ArrayList<>();
}
