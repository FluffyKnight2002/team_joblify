package com.ace_inspiration.team_joblify.entity;

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
@Table(name = "candidate")
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

    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime applyDate;

    @Lob
    @Column(columnDefinition = "longblob", nullable = false)
    private String resume;

    @Column(length = 100, nullable = false)
    private String type;

    @Column(columnDefinition = "longtext")
    private String note;

    @OneToOne(orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "summary_id")
    private Summary summary;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interview>interviews=new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacancy_info_id")
    private VacancyInfo vacancyInfo;
    
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)  
    private List<OfferMailSended> offerMail = new ArrayList<>();;

}
