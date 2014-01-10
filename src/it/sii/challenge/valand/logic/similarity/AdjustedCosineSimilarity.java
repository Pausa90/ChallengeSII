package it.sii.challenge.valand.logic.similarity;


import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.utilities.MapsListsUtilities;

import java.util.Map;

public class AdjustedCosineSimilarity implements SimilarityCalculator {
		//DA USARE CON ITEM BASED!


		@Override
		public double calculateSimilarity(Map<String, Integer> user1, Map<String, Integer> user2) {
			double similarity_numerator = 0;
            double similarity_denominator_p1 = 0;
            double similarity_denominator_p2 = 0;
            
            Integer value1;
            Integer value2;
            MapsListsUtilities utilities = new MapsListsUtilities();

            
            for (String key : utilities.unionListOfKeySets(user1, user2)){
                    value1 = user1.get(key);
                    value2 = user2.get(key);
                    if (value1==null)
                            value1 = 0;
                    if (value2==null)
                            value2 = 0;
                    similarity_numerator += (value1*value2);
            }               
            
            for (String key : user1.keySet())
                    similarity_denominator_p1 += Math.pow(user1.get(key), 2);

            for (String key : user2.keySet())
                    similarity_denominator_p2 += Math.pow(user2.get(key), 2);
                    
            
            return similarity_numerator/Math.sqrt(similarity_denominator_p1 * similarity_denominator_p2);
		}

		@Override
		public double calculateSimilarity(Business b1, Business b2) {
			// TODO Auto-generated method stub
			return 0;
		}

}