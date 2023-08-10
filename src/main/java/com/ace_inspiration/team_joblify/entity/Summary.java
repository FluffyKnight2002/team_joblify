package com.ace_inspiration.team_joblify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;


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
//    @JsonIgnoreProperties(value={"hibernateLazyInitializer","summary"})
    @JsonBackReference
    private Candidate candidate;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "summary_languageSkills", joinColumns = @JoinColumn(name = "summary_id"),
    inverseJoinColumns = @JoinColumn(name = "languageSkills_id"))
//    @JsonIgnoreProperties(value={"hibernateLazyInitializer","summary"})
    @JsonManagedReference
    private List<LanguageSkills> languageSkills= new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "summary_techSkills", joinColumns = @JoinColumn(name = "summary_id"),
            inverseJoinColumns = @JoinColumn(name = "techSkills_id"))
//    @JsonIgnoreProperties(value={"hibernateLazyInitializer","summary"})
    @JsonManagedReference
    private List<TechSkills> techSkills= new ArrayList<>();
}
