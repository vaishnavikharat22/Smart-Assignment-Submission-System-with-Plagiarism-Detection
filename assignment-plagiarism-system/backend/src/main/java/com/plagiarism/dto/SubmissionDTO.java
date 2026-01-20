package com.plagiarism.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionDTO {
    private Long id;
    private Long assignmentId;
    private Long studentId;
    private String studentName;
    private String fileName;
    private String status;
    private Integer score;
    private String feedback;
    private LocalDateTime submittedAt;
    private PlagiarismResultDTO plagiarismResult;
}
