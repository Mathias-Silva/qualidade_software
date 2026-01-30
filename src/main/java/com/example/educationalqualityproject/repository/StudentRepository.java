package com.example.educationalqualityproject.repository;

import com.example.educationalqualityproject.entity.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    boolean existsByEmail(String email);
    boolean existsByRegistrationNumber(String registrationNumber);
}