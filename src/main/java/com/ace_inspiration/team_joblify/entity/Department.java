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

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "department")
public class Department implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 75, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "department", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("department")
    private List<User> user=new ArrayList<>();

    @OneToMany(mappedBy = "department", orphanRemoval = true , cascade = CascadeType.ALL)
    private List<Vacancy> vacancies =new ArrayList<>();
}
