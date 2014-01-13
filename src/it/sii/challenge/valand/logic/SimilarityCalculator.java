package it.sii.challenge.valand.logic;

import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.User;
import it.sii.challenge.valand.utilities.MapsListsUtilities;

import java.util.Map;

public class SimilarityCalculator {

	//TODO: provare a migliorare sulla base della grandezza della colonna/riga
	
	public double doPearsonSimilarity(Map<Business, Integer> user1, Map<Business, Integer> user2, 
			double averageRating1, double averageRating2) {
		double similarity_numerator = 0;
		double similarity_denominator_p1 = 0;
		double similarity_denominator_p2 = 0;

		Integer rating1;
		Integer rating2;
		MapsListsUtilities<Business> utilities = new MapsListsUtilities<Business>();

		for (Business key : utilities.unionListOfKeySets(user1, user2)){
			rating1 = user1.get(key);
			rating2 = user2.get(key);
			if (rating1==null)
				rating1 = 0;
			if (rating2==null)
				rating2 = 0;
			similarity_numerator += ( (rating1 - averageRating1) * (rating2 - averageRating2));
		}               

		for (Business key : user1.keySet())
			similarity_denominator_p1 += Math.pow(user1.get(key)-averageRating1, 2);

		for (Business key : user2.keySet())
			similarity_denominator_p2 += Math.pow(user2.get(key)-averageRating2, 2);


		return similarity_numerator/Math.sqrt(similarity_denominator_p1 * similarity_denominator_p2);
	}
	
	public double doAdjustedCosineSimilarity(Map<User, Integer> columnB1, Map<User, Integer> columnB2) {
		double similarity_numerator = 0;
		double similarity_denominator_p1 = 0;
		double similarity_denominator_p2 = 0;

		Integer rating1;
		Integer rating2;
		
		MapsListsUtilities<User> utilities = new MapsListsUtilities<User>();

		for (User user : utilities.unionListOfKeySets(columnB1, columnB2)){
			rating1 = columnB1.get(user);
			rating2 = columnB2.get(user);
			if (rating1==null)
				rating1 = 0;
			if (rating2==null)
				rating2 = 0;
			similarity_numerator += ( (rating1 - user.getAverageStars()) * (rating2 - user.getAverageStars()));
		}               

		for (User user : columnB1.keySet())
			similarity_denominator_p1 += Math.pow(columnB1.get(user)-user.getAverageStars(), 2);

		for (User user : columnB2.keySet())
			similarity_denominator_p2 += Math.pow(columnB2.get(user)-user.getAverageStars(), 2);


		return similarity_numerator/Math.sqrt(similarity_denominator_p1 * similarity_denominator_p2);
	}

}