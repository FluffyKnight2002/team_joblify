package com.ace_inspiration.team_joblify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobPostDepartment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 24, nullable = false)
    private String salary;

    @Column(nullable = false)
    private int requiredVacancies;

    @Column(nullable = false)
    private int hiredVacancies;

    @Column(length = 10, nullable = false)
    private String jobType;

    @Column(nullable = false)
    private Date openDate;

    @Column(nullable = false)
    private Date closeDate;

    @Column(nullable = false)
    private Date updatedDate;

    @Column()
    private Level lvl;

    @Column(columnDefinition = "longtext")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updatedUser_id")
    private User updatedUser;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "jobPost_id")
    private JobPost jobPost;

    @OneToMany(mappedBy = "jobPostDepartment",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Action> actions = new ArrayList<>();
}
