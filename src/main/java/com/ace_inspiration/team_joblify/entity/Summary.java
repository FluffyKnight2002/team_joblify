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
public class Summary implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Date dob;

    @Column(length = 15, nullable = false)
    private String phone;

    @Column(nullable = false)
    private String skills;

    @Column(nullable = false)
    private String experience;

    @Column(length = 30, nullable = false, unique = true)
    private String email;

    @OneToOne(mappedBy = "summary", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Candidate candidate;
}
