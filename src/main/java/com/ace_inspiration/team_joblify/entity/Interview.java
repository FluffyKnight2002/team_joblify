package com.ace_inspiration.team_joblify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Interview implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 15)
    @Enumerated(EnumType.STRING)
    private InterviewStage interviewStage;

    @Column(length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    private InterviewType type;

    @Column(length = 50, nullable = false)
    private String interviewTime;

    @Column(length = 50, nullable = false)
    private String interviewDate;

    @Column(columnDefinition = "longtext")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;
}
