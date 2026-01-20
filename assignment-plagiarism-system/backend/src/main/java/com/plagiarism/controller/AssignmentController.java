package com.plagiarism.controller;

import com.plagiarism.dto.AssignmentDTO;
import com.plagiarism.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> createAssignment(@RequestBody Map<String, Object> request) {
        try {
            String title = (String) request.get("title");
            String description = (String) request.get("description");
            Long teacherId = ((Number) request.get("teacherId")).longValue();
            String dueDateStr = (String) request.get("dueDate");
            Integer maxScore = request.get("maxScore") != null ? ((Number) request.get("maxScore")).intValue() : 100;

            LocalDateTime dueDate = LocalDateTime.parse(dueDateStr);

            var assignment = assignmentService.createAssignment(title, description, teacherId, dueDate, maxScore);
            return ResponseEntity.ok(assignmentService.convertToDTO(assignment));
        } catch (Exception e) {
            log.error("Error creating assignment", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<AssignmentDTO>> getAllAssignments() {
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAssignmentById(@PathVariable Long id) {
        try {
            var assignment = assignmentService.getAssignmentById(id);
            return ResponseEntity.ok(assignmentService.convertToDTO(assignment));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<AssignmentDTO>> getAssignmentsByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByTeacher(teacherId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> updateAssignment(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String title = (String) request.get("title");
            String description = (String) request.get("description");
            Integer maxScore = request.get("maxScore") != null ? ((Number) request.get("maxScore")).intValue() : null;
            LocalDateTime dueDate = null;
            
            if (request.get("dueDate") != null) {
                dueDate = LocalDateTime.parse((String) request.get("dueDate"));
            }

            var assignment = assignmentService.updateAssignment(id, title, description, dueDate, maxScore);
            return ResponseEntity.ok(assignmentService.convertToDTO(assignment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> deleteAssignment(@PathVariable Long id) {
        try {
            assignmentService.deleteAssignment(id);
            return ResponseEntity.ok("Assignment deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
