package it.sii.challenge.valand.logic.similarity;


import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.utilities.MapsListsUtilities;

import java.util.Map;

public class AdjustedCosineSimilarity implements SimilarityCalculator {
	
		/**
		 * Se si vuole lanciare CosineSimilarity: user1 user2 
		 * Se si vuole lanciare CosineSimilarity: business1 business2 
		 */
		@Override
		public double calculateSimilarity(Map<String, Integer> user1, Map<String, Integer> user2, 
												Integer userAverageRating1, Integer userAverageRating2) {
			double similarity_numerator = 0;
            double similarity_denominator_p1 = 0;
            double similarity_denominator_p2 = 0;
            
            Integer rating1;
            Integer rating2;
            MapsListsUtilities utilities = new MapsListsUtilities();
                        
            for (String key : utilities.unionListOfKeySets(user1, user2)){
                    rating1 = user1.get(key);
                    rating2 = user2.get(key);
                    if (rating1==null)
                            rating1 = 0;
                    if (rating2==null)
                            rating2 = 0;
                    similarity_numerator += ( (rating1 - userAverageRating1) * (rating2 - userAverageRating2));
            }               
            
            for (String key : user1.keySet())
                    similarity_denominator_p1 += Math.pow(user1.get(key)-userAverageRating1, 2);

            for (String key : user2.keySet())
                    similarity_denominator_p2 += Math.pow(user2.get(key)-userAverageRating2, 2);
                    
            
            return similarity_numerator/Math.sqrt(similarity_denominator_p1 * similarity_denominator_p2);
		}

}