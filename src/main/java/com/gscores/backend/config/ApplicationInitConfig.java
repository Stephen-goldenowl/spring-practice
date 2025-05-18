package com.gscores.backend.config;

import com.gscores.backend.entity.Result;
import com.gscores.backend.entity.Student;
import com.gscores.backend.entity.Subject;
import com.gscores.backend.repository.StudentRepository;
import com.gscores.backend.repository.SubjectRepository;
import com.gscores.backend.service.CSVSeederService;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Configuration
@RequiredArgsConstructor
public class ApplicationInitConfig {
    private final Logger logger = Logger.getLogger(ApplicationInitConfig.class.getName());

    @Bean
    ApplicationRunner applicationRunner(StudentRepository studentRepository,
                                        SubjectRepository subjectRepository,
                                        CSVSeederService csvSeederService) {
        return args -> {
            if (studentRepository.count() > 0) {
                logger.info("Database already contains data. Seeding skipped.");
                return;
            }

            logger.info("Database is empty. Seeding data...");

            Map<String, Subject> subjectMap = new HashMap<>();
            String[] subjects = {
                    "Mathematics", "Literature", "Foreign Language",
                    "Physics", "Chemistry", "Biology",
                    "History", "Geography", "Civic Education"
            };

            for (String subjectName : subjects) {
                subjectMap.put(subjectName, subjectRepository.findByName(subjectName)
                        .orElseGet(() -> subjectRepository.save(new Subject(subjectName))));
            }

            final int batchSize = 10000;
            List<Student> students = new ArrayList<>();
            List<Result> results = new ArrayList<>();

            try (CSVReader reader = new CSVReader(new InputStreamReader(new ClassPathResource("diem_thi_thpt_2024.csv").getInputStream()))) {
                String[] row;
                reader.readNext();
                int count = 0;

                while ((row = reader.readNext()) != null) {
                    String sbd = row[0];
                    String foreignLanguage = row[row.length - 1];

                    Student student = new Student(sbd, foreignLanguage);
                    students.add(student);

                    for (int i = 1; i <= subjects.length; i++) {
                        if (!row[i].isEmpty()) {
                            Double score = Double.parseDouble(row[i]);
                            results.add(new Result(student, subjectMap.get(subjects[i - 1]), score));
                        }
                    }

                    count++;
                    if (count % batchSize == 0) {
                        csvSeederService.saveBatch(students, results);
                        students.clear();
                        results.clear();
                        logger.info("Inserted " + count + " records...");
                    }
                }

                if (!students.isEmpty()) {
                    csvSeederService.saveBatch(students, results);
                }

            } catch (Exception e) {
                System.err.println("Seeding failed: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        };
    }


}