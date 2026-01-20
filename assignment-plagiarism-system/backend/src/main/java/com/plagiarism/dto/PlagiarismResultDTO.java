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
public class PlagiarismResultDTO {
    private Long id;
    private Double similarityScore;
    private Integer totalComparisons;
    private String detailedReport;
    private LocalDateTime checkedAt;
}
