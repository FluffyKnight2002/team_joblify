package com.ace_inspiration.team_joblify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Department implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 75, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "department", orphanRemoval = true, fetch = FetchType.LAZY)
//    @JsonIgnoreProperties(value={"hibernateLazyInitializer","department"})
    @JsonManagedReference
    private List<User> user=new ArrayList<>();

    @OneToMany(mappedBy = "department", orphanRemoval = true, fetch = FetchType.LAZY)
//    @JsonIgnoreProperties(value={"hibernateLazyInitializer","department"})
    @JsonManagedReference
    private List<Vacancy> vacancies =new ArrayList<>();
}
