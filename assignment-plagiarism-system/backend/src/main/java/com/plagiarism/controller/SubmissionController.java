package com.plagiarism.controller;

import com.plagiarism.dto.SubmissionDTO;
import com.plagiarism.service.PlagiarismService;
import com.plagiarism.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class SubmissionController {

    private final SubmissionService submissionService;
    private final PlagiarismService plagiarismService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> uploadSubmission(
            @RequestParam Long assignmentId,
            @RequestParam Long studentId,
            @RequestParam("file") MultipartFile file) {
        try {
            var submission = submissionService.submitAssignment(assignmentId, studentId, file);
            return ResponseEntity.ok(submissionService.convertToDTO(submission));
        } catch (Exception e) {
            log.error("Error uploading submission", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSubmission(@PathVariable Long id) {
        try {
            var submission = submissionService.getSubmissionById(id);
            return ResponseEntity.ok(submissionService.convertToDTO(submission));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<SubmissionDTO>> getSubmissionsByAssignment(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByAssignment(assignmentId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<SubmissionDTO>> getSubmissionsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByStudent(studentId));
    }

    @PostMapping("/{submissionId}/grade")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> gradeSubmission(
            @PathVariable Long submissionId,
            @RequestBody Map<String, Object> request) {
        try {
            Integer score = ((Number) request.get("score")).intValue();
            String feedback = (String) request.get("feedback");

            var submission = submissionService.gradeSubmission(submissionId, score, feedback);
            return ResponseEntity.ok(submissionService.convertToDTO(submission));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{submissionId}/check-plagiarism")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> checkPlagiarism(@PathVariable Long submissionId) {
        try {
            var result = plagiarismService.checkPlagiarism(submissionId);
            return ResponseEntity.ok(plagiarismService.convertResultToDTO(result));
        } catch (Exception e) {
            log.error("Error checking plagiarism", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{submissionId}/plagiarism")
    public ResponseEntity<?> getPlagiarismResult(@PathVariable Long submissionId) {
        try {
            var result = plagiarismService.getPlagiarismResult(submissionId);
            if (result == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(plagiarismService.convertResultToDTO(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/assignment/{assignmentId}/plagiarism-results")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getPlagiarismResultsForAssignment(@PathVariable Long assignmentId) {
        try {
            var results = plagiarismService.getAllPlagiarismResultsForAssignment(assignmentId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
