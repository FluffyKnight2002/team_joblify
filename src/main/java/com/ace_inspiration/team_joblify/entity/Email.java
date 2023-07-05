package com.ace_inspiration.team_joblify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Email implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 75, nullable = false)
    private String subject;

    @Column(columnDefinition = "longtext", nullable = false)
    private String body;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private InterviewType interviewType;

    private Date interviewDate;

    private Date interviewTime;

    @Column(nullable = false)
    private Date sentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id")
    private Applicant applicant;

    @OneToOne(mappedBy = "email", cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
    private OnlineInterview onlineInterview;
}
