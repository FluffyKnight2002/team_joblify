package com.ace_inspiration.team_joblify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.generator.Generator;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Candidate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private Status selectionStatus;

    @Column(nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private  Status interviewStatus;

    @Column(nullable = false)
    private LocalDateTime applyDate;

    @Lob
    @Column(columnDefinition = "longblob", nullable = true)
    private String resume;

    @Column(columnDefinition = "longtext")
    private String note;

    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "summary_id", unique = true)
//    @JsonIgnoreProperties(value={"hibernateLazyInitializer","candidate"})
    @JsonManagedReference
    private Summary summary;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    @JsonIgnoreProperties(value={"hibernateLazyInitializer","candidate"})
    @JsonManagedReference
    private List<Interview>interviews=new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
<<<<<<< HEAD
    @JoinColumn(name = "vacancy_department_id")
//    @JsonIgnoreProperties(value={"hibernateLazyInitializer","candidate"})
    @JsonManagedReference
    private VacancyDepartment vacancyDepartment;
=======
    @JoinColumn(name = "vacancy_info_id")
//    @JsonIgnoreProperties(value={"hibernateLazyInitializer","candidate"})
    @JsonManagedReference
    private VacancyInfo vacancyInfo;
>>>>>>> backup
}
