package com.gscores.backend.service;

import com.gscores.backend.entity.Result;
import com.gscores.backend.entity.Student;
import com.gscores.backend.repository.ResultRepository;
import com.gscores.backend.repository.StudentRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CSVSeederService {
    private final StudentRepository studentRepository;
    private final ResultRepository resultRepository;
    private final EntityManager entityManager;

    @Transactional
    public void saveBatch(List<Student> students, List<Result> results) {
        studentRepository.saveAll(students);
        resultRepository.saveAll(results);
        entityManager.flush();
        entityManager.clear();
    }
}
