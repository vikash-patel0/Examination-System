package com.example.ExaminationSystem.controller;


import com.example.ExaminationSystem.model.Exam;
import com.example.ExaminationSystem.model.User;
import com.example.ExaminationSystem.service.ExamService;
import com.example.ExaminationSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/exams")
public class ExamApiController {

    @Autowired
    private ExamService examService;

    @Autowired
    private UserService userService;

    // ✅ 1. Get ALL exams for students
    @GetMapping("/all")
    public List<Exam> getAllExams() {
        return examService.getAllExams();
    }

    // ✅ 2. Get exams by educator ID (secured browser does session check, this uses ID)
    @GetMapping("/educator/{educatorId}")
    public List<Exam> getExamsByEducator(@PathVariable Long educatorId) {
        User educator = userService.getUserById(educatorId)
                .orElseThrow(() -> new IllegalArgumentException("Educator not found"));
        return examService.getExamsByCreator(educator);
    }

    // ✅ 3. Get a specific exam by ID
    @GetMapping("/{examId}")
    public Exam getExam(@PathVariable Long examId) {
        return examService.getExamById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam ID not found: " + examId));
    }

    // ✅ 4. Create a new exam (educator ID in createdBy)
    @PostMapping("/create")
    public Exam createExam(@RequestBody Exam exam) {
        // Expecting exam JSON to contain createdBy.id field
        if (exam.getCreatedBy() == null || exam.getCreatedBy().getId() == null) {
            throw new IllegalArgumentException("Educator (createdBy) is required");
        }

        User educator = userService.getUserById(exam.getCreatedBy().getId())
                .orElseThrow(() -> new IllegalArgumentException("Educator not found"));

        exam.setCreatedBy(educator);
        return examService.createExam(exam);
    }

    // ✅ 5. Update an exam
    @PutMapping("/update")
    public Exam updateExam(@RequestBody Exam exam) {
        if (exam.getId() == null) {
            throw new IllegalArgumentException("Exam ID is required");
        }

        Exam existing = examService.getExamById(exam.getId())
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        // Optional ownership validation
        if (!existing.getCreatedBy().getId().equals(exam.getCreatedBy().getId())) {
            throw new IllegalArgumentException("You are not authorized to update this exam");
        }

        return examService.updateExam(exam);
    }

    // ✅ 6. Delete an exam by ID
    @DeleteMapping("/delete/{examId}")
    public String deleteExam(@PathVariable Long examId) {
        examService.deleteExam(examId);
        return "Deleted exam with ID: " + examId;
    }
}

