package com.gscores.backend.repository;

import com.gscores.backend.entity.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByRegistrationNumber(String registrationNumber);

    @Query("""
    SELECT r.student, SUM(r.score) as totalScore
    FROM Result r
    WHERE r.subject.name IN :subjects
    GROUP BY r.student
    ORDER BY totalScore DESC
""")
    List<Object[]> findTopStudentsByGroup(@Param("subjects") List<String> subjects, Pageable pageable);
}
