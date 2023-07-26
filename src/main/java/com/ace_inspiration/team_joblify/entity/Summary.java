package com.ace_inspiration.team_joblify.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToOne(mappedBy = "summary", fetch = FetchType.LAZY, orphanRemoval = true)
    private Candidate candidate;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "summary_languageSkills", joinColumns = @JoinColumn(name = "summary_id"),
    inverseJoinColumns = @JoinColumn(name = "languageSkills_id"))
    private List<LanguageSkills> languageSkills;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "summary_techSkills", joinColumns = @JoinColumn(name = "summary_id"),
            inverseJoinColumns = @JoinColumn(name = "techSkills_id"))
    private List<TechSkills> techSkills= new ArrayList<>();
}