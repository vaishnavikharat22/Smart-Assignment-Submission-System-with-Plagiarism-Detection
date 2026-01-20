package com.plagiarism.service;

import com.plagiarism.dto.SubmissionDTO;
import com.plagiarism.entity.*;
import com.plagiarism.repository.SubmissionRepository;
import com.plagiarism.util.FileTextExtractor;
import com.plagiarism.util.PlagiarismDetectionEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentService assignmentService;
    private final UserService userService;
    private final FileTextExtractor fileTextExtractor;
    private final PlagiarismDetectionEngine plagiarismEngine;
    private final PlagiarismService plagiarismService;

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "submissions"
            + File.separator;

    public Submission submitAssignment(Long assignmentId, Long studentId, MultipartFile file) {
        log.info("Starting submission upload for assignment {} student {}", assignmentId, studentId);
        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        User student = userService.getUserById(studentId);

        // Delete existing submission if any
        log.info("Checking for existing submission");
        submissionRepository.findByAssignmentAndStudent(assignment, student).ifPresent(s -> {
            log.info("Deleting existing submission {}", s.getId());
            submissionRepository.delete(s);
        });

        // Save file
        log.info("Saving file: {}", file.getOriginalFilename());
        String filePath = saveFile(file);
        log.info("File saved to: {}", filePath);

        // Extract text from file
        log.info("Extracting text from file");
        String extractedText = fileTextExtractor.extractTextFromFile(filePath);
        log.info("Text extracted length: {}", extractedText.length());

        Submission submission = Submission.builder()
                .assignment(assignment)
                .student(student)
                .filePath(filePath)
                .fileName(file.getOriginalFilename())
                .extractedText(extractedText)
                .status(SubmissionStatus.SUBMITTED)
                .createdAt(LocalDateTime.now())
                .build();

        log.info("Saving submission to database");
        return submissionRepository.save(submission);
    }

    public Submission getSubmissionById(Long id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
    }

    public List<SubmissionDTO> getSubmissionsByAssignment(Long assignmentId) {
        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        return submissionRepository.findByAssignment(assignment).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SubmissionDTO> getSubmissionsByStudent(Long studentId) {
        User student = userService.getUserById(studentId);
        return submissionRepository.findByStudent(student).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Submission gradeSubmission(Long submissionId, Integer score, String feedback) {
        Submission submission = getSubmissionById(submissionId);
        submission.setScore(score);
        submission.setFeedback(feedback);
        submission.setStatus(SubmissionStatus.GRADED);
        return submissionRepository.save(submission);
    }

    public Submission updateSubmissionStatus(Long submissionId, SubmissionStatus status) {
        Submission submission = getSubmissionById(submissionId);
        submission.setStatus(status);
        return submissionRepository.save(submission);
    }

    public SubmissionDTO convertToDTO(Submission submission) {
        SubmissionDTO dto = SubmissionDTO.builder()
                .id(submission.getId())
                .assignmentId(submission.getAssignment().getId())
                .studentId(submission.getStudent().getId())
                .studentName(submission.getStudent().getFullName())
                .fileName(submission.getFileName())
                .status(submission.getStatus().toString())
                .score(submission.getScore())
                .feedback(submission.getFeedback())
                .submittedAt(submission.getCreatedAt())
                .build();

        if (submission.getPlagiarismResult() != null) {
            dto.setPlagiarismResult(plagiarismService.convertResultToDTO(submission.getPlagiarismResult()));
        }

        return dto;
    }

    private String saveFile(MultipartFile file) {
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.write(filePath, file.getBytes());
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }
}
