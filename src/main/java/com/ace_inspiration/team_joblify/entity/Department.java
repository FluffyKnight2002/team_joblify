package com.ace_inspiration.team_joblify.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Department implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 75, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "department", orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<User> user=new ArrayList<>();

    @OneToMany(mappedBy = "department", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Vacancy> vacancies =new ArrayList<>();
}
