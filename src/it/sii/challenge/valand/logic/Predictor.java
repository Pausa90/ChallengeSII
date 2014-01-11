package it.sii.challenge.valand.logic;

import java.util.List;

import it.sii.challenge.valand.logic.algorithm.ClassificationAlgorithm;
import it.sii.challenge.valand.logic.algorithm.KNNAlgorithm;
import it.sii.challenge.valand.logic.similarity.SimilarityCalculator;
import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.model.User;
import it.sii.challenge.valand.model.UserBusinessMatrix;

public class Predictor {

	private UserBusinessMatrix matrix;
	private List<Review> reviewsToTest;
	private ClassificationAlgorithm algorithm;
	private final int SOGLIA = 5;
	private final int AVERAGE_VALUE = 3;

	public Predictor(UserBusinessMatrix matrix, List<Review> reviewsToTest) {
		this.setMatrix(matrix);
		this.reviewsToTest = reviewsToTest;
	}

	public UserBusinessMatrix getMatrix() {
		return matrix;
	}

	public void setMatrix(UserBusinessMatrix matrix) {
		this.matrix = matrix;
	}
	
	public String startPrediction(int k){		
		String allPrediction = ""; //Salva ogni predizione su ogni riga
		this.algorithm = new KNNAlgorithm(k);
				
		for (Review review : this.reviewsToTest){
			int prediction = this.predict(k, review);
			allPrediction += prediction + "\n";
		}	
		
		return allPrediction;
	}
	
	/**
	 * predict(String user_id, String business_id)
	 * 		User user = getUser(user_id)
	 * 		Business business = getBusiness(user_id)
	 * 
	 * 	if (user !== null && business !=null)
	 * 		lanciaCombinazione(user,business)
	 * else (user !== null)
	 * 		lanciaUserBased(user)
	 * else (business !== null)
	 * 		lanciaItemBased(business)
	 * else
	 * 		rating = 3
	 * 
	 *  
	 *  lancia*Based(T)
	 *  if neighborhood < soglia
	 *  	return 3
	 *  enterTheMatrix
	 * @param review 
	 * @param algorithm 
	 * 
	 */
	
	private int predict(int k, Review review){
		User user = this.matrix.getUser(review.getUserId());
		Business business = this.matrix.getBusiness(review.getBusinessId());
		
		if (user!=null && business!=null)
			return this.linearCombinationPrediction(review);
		else if (user!=null)
			return this.userBasedPrediction(review);
		else if (business!=null)
			return this.itemBasedPrediction(review);
		return AVERAGE_VALUE;
	}

	private int linearCombinationPrediction(Review review) {
		List<User> userNeighborhood = algorithm.getNeighborHood(this.matrix, this.matrix.getUser(review.getUserId()));
		List<Business> buisnessNeighborhood = algorithm.getNeighborHood(this.matrix, this.matrix.getBusiness(review.getBusinessId()));
		
		//Parametro che stabilisce dinamicamente quanto fidarsi delle predizioni
		int lambda = this.calculateLambda(userNeighborhood.size(), buisnessNeighborhood.size());
		
		int userPedict = userBasedPrediction(review, userNeighborhood);
		int buisnessPedict = itemBasedPrediction(review, buisnessNeighborhood);
		
		return lambda*userPedict + (1-lambda)*buisnessPedict;
	}

	private int calculateLambda(int userSize, int buisnessSize) {
		if (userSize < SOGLIA)
			return 0;
		if (buisnessSize < SOGLIA)
			return 1;
		
		return userSize / (userSize+buisnessSize); //us : us+bus = x : 1
	}

	private int userBasedPrediction(Review review) {		
		List<User> neighborhood = algorithm.getNeighborHood(this.matrix, this.matrix.getUser(review.getUserId()));
		return this.userBasedPrediction(review, neighborhood);
	}

	private int itemBasedPrediction(Review review) {
		List<Business> neighborhood = algorithm.getNeighborHood(this.matrix, this.matrix.getBusiness(review.getBusinessId()));
		return this.itemBasedPrediction(review, neighborhood);
	}
	
	private int userBasedPrediction(Review review, List<User> neighborhood) {
		if (neighborhood.size() < SOGLIA)
			return AVERAGE_VALUE;
		return algorithm.userBasedPrediction(neighborhood, review);
	}
	
	private int itemBasedPrediction(Review review, List<Business> neighborhood) {
		if (neighborhood.size() < SOGLIA)
			return AVERAGE_VALUE;
		return algorithm.itemBasedPrediction(neighborhood, review);
	}

}
