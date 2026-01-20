package com.plagiarism.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "plagiarism_results")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlagiarismResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;

    @Column(name = "similarity_score")
    private Double similarityScore; // 0-100 percentage

    @Column(name = "total_comparisons")
    private Integer totalComparisons;

    @Column(columnDefinition = "LONGTEXT")
    private String detailedReport;

    @Column(columnDefinition = "LONGTEXT")
    private String highlightedText; // Text with highlighted copied portions

    @CreationTimestamp
    private LocalDateTime checkedAt;
}
