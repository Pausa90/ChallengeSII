package it.sii.challenge.valand.logic.similarity;

import java.util.Map;

public interface SimilarityCalculator {

    public double calculateSimilarity(Map<String, Integer> user1, Map<String, Integer> user2,
    										Integer averageRating1, Integer averageRating2);
     
}