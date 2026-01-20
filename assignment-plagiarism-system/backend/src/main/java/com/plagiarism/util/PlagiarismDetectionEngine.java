package com.plagiarism.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PlagiarismDetectionEngine {

    private static final int N_GRAM_SIZE = 3;

    /**
     * Calculate similarity between two texts using cosine similarity
     */
    public double calculateSimilarity(String text1, String text2) {
        if (text1 == null || text2 == null || text1.isEmpty() || text2.isEmpty()) {
            return 0.0;
        }

        // Normalize texts
        text1 = normalizeText(text1);
        text2 = normalizeText(text2);

        // Get n-grams
        Set<String> nGrams1 = generateNGrams(text1, N_GRAM_SIZE);
        Set<String> nGrams2 = generateNGrams(text2, N_GRAM_SIZE);

        if (nGrams1.isEmpty() || nGrams2.isEmpty()) {
            return 0.0;
        }

        // Calculate intersection and union
        Set<String> intersection = new HashSet<>(nGrams1);
        intersection.retainAll(nGrams2);

        Set<String> union = new HashSet<>(nGrams1);
        union.addAll(nGrams2);

        // Jaccard similarity
        double jaccardSimilarity = (double) intersection.size() / union.size();

        // Cosine similarity based on token frequency
        double cosineSimilarity = calculateCosineSimilarity(text1, text2);

        // Combine both metrics for more accurate result
        return (jaccardSimilarity * 0.4 + cosineSimilarity * 0.6) * 100;
    }

    /**
     * Generate n-grams from text
     */
    private Set<String> generateNGrams(String text, int n) {
        String[] words = text.split("\\s+");
        Set<String> nGrams = new HashSet<>();

        for (int i = 0; i <= words.length - n; i++) {
            StringBuilder nGram = new StringBuilder();
            for (int j = i; j < i + n; j++) {
                if (j > i) nGram.append(" ");
                nGram.append(words[j]);
            }
            nGrams.add(nGram.toString());
        }

        return nGrams;
    }

    /**
     * Calculate cosine similarity based on term frequencies
     */
    private double calculateCosineSimilarity(String text1, String text2) {
        Map<String, Integer> vector1 = getTermFrequencyVector(text1);
        Map<String, Integer> vector2 = getTermFrequencyVector(text2);

        double dotProduct = 0.0;
        for (String term : vector1.keySet()) {
            if (vector2.containsKey(term)) {
                dotProduct += vector1.get(term) * vector2.get(term);
            }
        }

        double magnitude1 = Math.sqrt(vector1.values().stream()
                .mapToDouble(v -> v * v).sum());
        double magnitude2 = Math.sqrt(vector2.values().stream()
                .mapToDouble(v -> v * v).sum());

        if (magnitude1 == 0 || magnitude2 == 0) {
            return 0.0;
        }

        return dotProduct / (magnitude1 * magnitude2);
    }

    /**
     * Get term frequency vector for a text
     */
    private Map<String, Integer> getTermFrequencyVector(String text) {
        String[] words = text.split("\\s+");
        Map<String, Integer> termFrequency = new HashMap<>();

        for (String word : words) {
            if (!word.isEmpty()) {
                termFrequency.put(word, termFrequency.getOrDefault(word, 0) + 1);
            }
        }

        return termFrequency;
    }

    /**
     * Normalize text for comparison
     */
    private String normalizeText(String text) {
        return text
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "") // Remove special characters
                .replaceAll("\\s+", " ") // Remove extra whitespace
                .trim();
    }

    /**
     * Find and highlight similar sections between two texts
     */
    public String highlightSimilarSections(String originalText, String comparisonText) {
        String[] originalWords = normalizeText(originalText).split("\\s+");
        String[] comparisonWords = normalizeText(comparisonText).split("\\s+");

        Set<String> comparisonSet = new HashSet<>(Arrays.asList(comparisonWords));

        StringBuilder highlighted = new StringBuilder();
        for (int i = 0; i < originalWords.length; i++) {
            if (i > 0) highlighted.append(" ");
            
            if (comparisonSet.contains(originalWords[i])) {
                highlighted.append("[SIMILAR:").append(originalWords[i]).append("]");
            } else {
                highlighted.append(originalWords[i]);
            }
        }

        return highlighted.toString();
    }

    /**
     * Calculate similarity with multiple texts and return detailed report
     */
    public Map<String, Object> calculateDetailedSimilarity(String submissionText, List<String> comparisonTexts) {
        Map<String, Object> result = new HashMap<>();
        List<Double> scores = new ArrayList<>();
        double maxScore = 0;
        int matchCount = 0;

        for (String comparisonText : comparisonTexts) {
            double similarity = calculateSimilarity(submissionText, comparisonText);
            scores.add(similarity);

            if (similarity > 0) {
                matchCount++;
            }
            if (similarity > maxScore) {
                maxScore = similarity;
            }
        }

        double averageScore = scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        result.put("similarityScore", Math.round(averageScore * 100.0) / 100.0);
        result.put("maxScore", Math.round(maxScore * 100.0) / 100.0);
        result.put("averageScore", Math.round(averageScore * 100.0) / 100.0);
        result.put("totalComparisons", comparisonTexts.size());
        result.put("matchedComparisons", matchCount);

        return result;
    }
}
