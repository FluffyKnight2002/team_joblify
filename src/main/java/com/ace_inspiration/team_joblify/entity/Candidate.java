package com.ace_inspiration.team_joblify.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Candidate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 15)
    @Enumerated(value = EnumType.STRING)
    private Status selectionStatus;

    @Column(nullable = false, length = 15)
    @Enumerated(value = EnumType.STRING)
    private  Status interviewStatus;

    @Column(nullable = false)
    private LocalDateTime applyDate;

    @Lob
    @Column(columnDefinition = "longblob", nullable = false)
    private String resume;

    @Column(columnDefinition = "longtext")
    private String note;

    @OneToOne(orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "summary_id")
    private Summary summary;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interview>interviews=new ArrayList<>();

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacancy_department_id")
    private VacancyDepartment vacancyDepartment;
}
