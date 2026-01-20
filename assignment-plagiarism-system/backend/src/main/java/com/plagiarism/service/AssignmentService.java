package com.plagiarism.service;

import com.plagiarism.dto.AssignmentDTO;
import com.plagiarism.entity.Assignment;
import com.plagiarism.entity.User;
import com.plagiarism.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final UserService userService;

    public Assignment createAssignment(String title, String description, Long teacherId, LocalDateTime dueDate, Integer maxScore) {
        User teacher = userService.getUserById(teacherId);
        
        Assignment assignment = Assignment.builder()
                .title(title)
                .description(description)
                .teacher(teacher)
                .dueDate(dueDate)
                .maxScore(maxScore != null ? maxScore : 100)
                .build();

        return assignmentRepository.save(assignment);
    }

    public Assignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
    }

    public List<AssignmentDTO> getAllAssignments() {
        return assignmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AssignmentDTO> getAssignmentsByTeacher(Long teacherId) {
        User teacher = userService.getUserById(teacherId);
        return assignmentRepository.findByTeacher(teacher).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Assignment updateAssignment(Long id, String title, String description, LocalDateTime dueDate, Integer maxScore) {
        Assignment assignment = getAssignmentById(id);
        
        if (title != null) assignment.setTitle(title);
        if (description != null) assignment.setDescription(description);
        if (dueDate != null) assignment.setDueDate(dueDate);
        if (maxScore != null) assignment.setMaxScore(maxScore);

        return assignmentRepository.save(assignment);
    }

    public void deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }

    public AssignmentDTO convertToDTO(Assignment assignment) {
        return AssignmentDTO.builder()
                .id(assignment.getId())
                .title(assignment.getTitle())
                .description(assignment.getDescription())
                .dueDate(assignment.getDueDate())
                .maxScore(assignment.getMaxScore())
                .teacherId(assignment.getTeacher().getId())
                .teacherName(assignment.getTeacher().getFullName())
                .createdAt(assignment.getCreatedAt())
                .build();
    }
}
