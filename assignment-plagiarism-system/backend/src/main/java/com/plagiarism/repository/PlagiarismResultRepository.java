package com.plagiarism.repository;

import com.plagiarism.entity.PlagiarismResult;
import com.plagiarism.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlagiarismResultRepository extends JpaRepository<PlagiarismResult, Long> {
    Optional<PlagiarismResult> findBySubmission(Submission submission);
}
