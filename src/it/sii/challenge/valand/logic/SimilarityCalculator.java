package it.sii.challenge.valand.logic;

import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.User;
import it.sii.challenge.valand.persistence.repository.UserRepository;
import it.sii.challenge.valand.persistence.repositoryImpl.UserRepositoryImpl;
import it.sii.challenge.valand.utilities.MapsListsUtilities;

import java.util.Map;

public class SimilarityCalculator {

	//TODO: provare a migliorare sulla base della grandezza della colonna/riga
	
	public double doPearsonSimilarity(Map<String, Integer> user1, Map<String, Integer> user2, 
			double averageRating1, double averageRating2) {
		double similarity_numerator = 0;
		double similarity_denominator_p1 = 0;
		double similarity_denominator_p2 = 0;

		Integer rating1;
		Integer rating2;
		MapsListsUtilities<String> utilities = new MapsListsUtilities<String>();

		for (String key : utilities.unionListOfKeySets(user1, user2)){
			rating1 = user1.get(key);
			rating2 = user2.get(key);
			if (rating1==null)
				rating1 = 0;
			if (rating2==null)
				rating2 = 0;
			similarity_numerator += ( (rating1 - averageRating1) * (rating2 - averageRating2));
		}               

		for (String key : user1.keySet())
			similarity_denominator_p1 += Math.pow(user1.get(key)-averageRating1, 2);

		for (String key : user2.keySet())
			similarity_denominator_p2 += Math.pow(user2.get(key)-averageRating2, 2);


		return similarity_numerator/Math.sqrt(similarity_denominator_p1 * similarity_denominator_p2);
	}
	
	public double doAdjustedCosineSimilarity(Map<String, Integer> columnB1, Map<String, Integer> columnB2) {
		double similarity_numerator = 0;
		double similarity_denominator_p1 = 0;
		double similarity_denominator_p2 = 0;
		UserRepository u_repo = new UserRepositoryImpl();
		Integer rating1;
		Integer rating2;
		
		MapsListsUtilities<String> utilities = new MapsListsUtilities<String>();

		for (String user : utilities.unionListOfKeySets(columnB1, columnB2)){
			rating1 = columnB1.get(user);
			rating2 = columnB2.get(user);
			if (rating1==null)
				rating1 = 0;
			if (rating2==null)
				rating2 = 0;
			User u = u_repo.findById(user);
			similarity_numerator += ( (rating1 - u.getAverageStars()) * (rating2 - u.getAverageStars()));
		}               

		for (String user : columnB1.keySet()){
			User u = u_repo.findById(user);
			similarity_denominator_p1 += Math.pow(columnB1.get(user)-u.getAverageStars(), 2);
		}

		for (String user : columnB2.keySet()){
			User u = u_repo.findById(user);
			similarity_denominator_p2 += Math.pow(columnB2.get(user)-u.getAverageStars(), 2);
		}


		return similarity_numerator/Math.sqrt(similarity_denominator_p1 * similarity_denominator_p2);
	}

}