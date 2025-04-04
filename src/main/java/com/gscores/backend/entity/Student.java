package com.gscores.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String registrationNumber;

    private String foreignLanguageCode;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Set<Result> results = new HashSet<>();

    public Student(String registrationNumber, String foreignLanguageCode) {
        this.registrationNumber = registrationNumber;
        this.foreignLanguageCode = foreignLanguageCode;
    }
}
