package com.plagiarism.service;

import com.plagiarism.dto.PlagiarismResultDTO;
import com.plagiarism.entity.PlagiarismResult;
import com.plagiarism.entity.Submission;
import com.plagiarism.entity.SubmissionStatus;
import com.plagiarism.repository.PlagiarismResultRepository;
import com.plagiarism.repository.SubmissionRepository;
import com.plagiarism.util.PlagiarismDetectionEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlagiarismService {

    private final PlagiarismResultRepository plagiarismResultRepository;
    private final SubmissionRepository submissionRepository;
    private final PlagiarismDetectionEngine plagiarismEngine;

    public PlagiarismResult checkPlagiarism(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        if (submission.getExtractedText() == null || submission.getExtractedText().isEmpty()) {
            throw new RuntimeException("No text extracted from submission");
        }

        submission.setStatus(SubmissionStatus.PLAGIARISM_CHECK_PENDING);
        submissionRepository.save(submission);

        // Get all other submissions for the same assignment
        List<Submission> otherSubmissions = submissionRepository
                .findByAssignmentId(submission.getAssignment().getId()).stream()
                .filter(s -> !s.getId().equals(submissionId) && s.getExtractedText() != null)
                .toList();

        // Extract texts for comparison
        List<String> comparisonTexts = new ArrayList<>();
        for (Submission other : otherSubmissions) {
            comparisonTexts.add(other.getExtractedText());
        }

        // Calculate detailed similarity
        Map<String, Object> similarityResult = plagiarismEngine.calculateDetailedSimilarity(
                submission.getExtractedText(),
                comparisonTexts
        );

        // Highlight similar sections
        String highlightedText = "";
        if (!comparisonTexts.isEmpty()) {
            highlightedText = plagiarismEngine.highlightSimilarSections(
                    submission.getExtractedText(),
                    comparisonTexts.get(0)
            );
        }

        // Create plagiarism result
        PlagiarismResult result = PlagiarismResult.builder()
                .submission(submission)
                .similarityScore((Double) similarityResult.get("similarityScore"))
                .totalComparisons((Integer) similarityResult.get("totalComparisons"))
                .detailedReport(generateDetailedReport(similarityResult))
                .highlightedText(highlightedText)
                .checkedAt(LocalDateTime.now())
                .build();

        plagiarismResultRepository.save(result);

        submission.setPlagiarismResult(result);
        submission.setStatus(SubmissionStatus.PLAGIARISM_CHECK_COMPLETE);
        submissionRepository.save(submission);

        log.info("Plagiarism check completed for submission: {}", submissionId);
        return result;
    }

    public PlagiarismResult getPlagiarismResult(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        return plagiarismResultRepository.findBySubmission(submission)
                .orElse(null);
    }

    public List<PlagiarismResultDTO> getAllPlagiarismResultsForAssignment(Long assignmentId) {
        List<Submission> submissions = submissionRepository.findByAssignmentId(assignmentId);
        return submissions.stream()
                .map(s -> plagiarismResultRepository.findBySubmission(s))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::convertResultToDTO)
                .sorted(Comparator.comparingDouble(PlagiarismResultDTO::getSimilarityScore).reversed())
                .toList();
    }

    public PlagiarismResultDTO convertResultToDTO(PlagiarismResult result) {
        return PlagiarismResultDTO.builder()
                .id(result.getId())
                .similarityScore(result.getSimilarityScore())
                .totalComparisons(result.getTotalComparisons())
                .detailedReport(result.getDetailedReport())
                .checkedAt(result.getCheckedAt())
                .build();
    }

    private String generateDetailedReport(Map<String, Object> similarityResult) {
        StringBuilder report = new StringBuilder();
        report.append("=== Plagiarism Detection Report ===\n");
        report.append(String.format("Similarity Score: %.2f%%\n", similarityResult.get("similarityScore")));
        report.append(String.format("Average Score: %.2f%%\n", similarityResult.get("averageScore")));
        report.append(String.format("Maximum Score: %.2f%%\n", similarityResult.get("maxScore")));
        report.append(String.format("Total Comparisons: %d\n", similarityResult.get("totalComparisons")));
        report.append(String.format("Matched Comparisons: %d\n", similarityResult.get("matchedComparisons")));

        Double score = (Double) similarityResult.get("similarityScore");
        if (score >= 80) {
            report.append("\n⚠️ CRITICAL: Very high similarity detected!");
        } else if (score >= 50) {
            report.append("\n⚠️ WARNING: High similarity detected.");
        } else if (score >= 20) {
            report.append("\nℹ️ NOTE: Moderate similarity detected.");
        } else {
            report.append("\n✓ Low similarity - likely original work.");
        }

        return report.toString();
    }
}
