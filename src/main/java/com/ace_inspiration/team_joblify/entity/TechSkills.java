package com.ace_inspiration.team_joblify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TechSkills {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 30, nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "techSkills", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Summary> summary;
}
