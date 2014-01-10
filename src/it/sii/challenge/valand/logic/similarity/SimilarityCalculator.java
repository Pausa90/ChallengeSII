package it.sii.challenge.valand.logic.similarity;

import it.sii.challenge.valand.model.Business;

import java.util.Map;

public interface SimilarityCalculator {

    public double calculateSimilarity(Business b1, Business b2);
    public double calculateSimilarity(Map<String, Integer> user1, Map<String, Integer> user2);
     
}