package com.gscores.backend.repository;

import com.gscores.backend.dto.model.ResultDTO;
import com.gscores.backend.entity.Result;
import com.gscores.backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ResultRepository extends JpaRepository<Result, Long> {
    @Query("SELECT r FROM Result r WHERE r.student.registrationNumber = :registrationNumber")
    Set<Result> findByStudentRegistrationNumber(String registrationNumber);

    @Query("SELECT COUNT(r) FROM Result r WHERE r.score >= 8 AND r.subject.name = :subjectName")
    Long countHighScores(@Param("subjectName") String subjectName);

    @Query("SELECT COUNT(r) FROM Result r WHERE r.score < 8 AND r.score >= 6 AND r.subject.name = :subjectName")
    Long countMediumHighScores(@Param("subjectName") String subjectName);

    @Query("SELECT COUNT(r) FROM Result r WHERE r.score < 6 AND r.score >= 4 AND r.subject.name = :subjectName")
    Long countMediumLowScores(@Param("subjectName") String subjectName);

    @Query("SELECT COUNT(r) FROM Result r WHERE r.score < 4 AND r.subject.name = :subjectName")
    Long countLowScores(@Param("subjectName") String subjectName);

}
