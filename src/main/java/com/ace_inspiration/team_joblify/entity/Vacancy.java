package com.ace_inspiration.team_joblify.entity;

import jakarta.persistence.*;
import lombok.*;


import java.io.Serializable;
import java.time.LocalDateTime;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Vacancy implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdUser_id")
//    @JsonIgnoreProperties(value={"hibernateLazyInitializer","createdVacancies"})
    @JsonManagedReference
    private User createdUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
//    @JsonIgnoreProperties(value={"hibernateLazyInitializer","vacancies"})
    @JsonManagedReference
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
//    @JsonIgnoreProperties(value={"hibernateLazyInitializer","vacancies"})
    @JsonManagedReference
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id")
//    @JsonIgnoreProperties(value={"hibernateLazyInitializer","vacancies"})
    @JsonBackReference
    private Department department;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "vacancy", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnoreProperties(value={"hibernateLazyInitializer","vacancy"})
    @JsonBackReference
<<<<<<< HEAD
    private List<VacancyDepartment> vacancyDepartment =new ArrayList<>();
=======
    private List<VacancyInfo> vacancyInfo =new ArrayList<>();
>>>>>>> backup

}
