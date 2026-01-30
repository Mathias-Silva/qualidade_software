package com.example.educationalqualityproject.controller.api;

import com.example.educationalqualityproject.entity.Student;
import com.example.educationalqualityproject.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*") // Allow cross-origin requests for external applications
public class StudentApiController {

    @Autowired
    private StudentService studentService;

    /**
     * Get all students
     */
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    /**
     * Get student by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable String id) {
        Optional<Student> student = studentService.getStudentById(id);
        if (student.isPresent()) {
            return new ResponseEntity<>(student.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create a new student
     */
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        // Check if email or registration number already exists
        if (studentService.existsByEmail(student.getEmail())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (studentService.existsByRegistrationNumber(student.getRegistrationNumber())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        
        Student savedStudent = studentService.saveStudent(student);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    /**
     * Update an existing student
     */
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable String id, @RequestBody Student student) {
        Optional<Student> existingStudent = studentService.getStudentById(id);
        if (!existingStudent.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // Check if email or registration number already exists for other students
        if (student.getEmail() != null && !student.getEmail().equals(existingStudent.get().getEmail())) {
            if (studentService.existsByEmail(student.getEmail())) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
        
        if (student.getRegistrationNumber() != null && !student.getRegistrationNumber().equals(existingStudent.get().getRegistrationNumber())) {
            if (studentService.existsByRegistrationNumber(student.getRegistrationNumber())) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
        
        student.setId(id); // Ensure the ID remains the same
        Student updatedStudent = studentService.saveStudent(student);
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }

    /**
     * Delete a student
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        Optional<Student> student = studentService.getStudentById(id);
        if (student.isPresent()) {
            studentService.deleteStudent(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}