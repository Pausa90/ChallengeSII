package it.sii.challenge.valand.logic;

import it.sii.challenge.valand.logic.algorithm.ClassificationAlgorithm;
import it.sii.challenge.valand.logic.algorithm.KNNAlgorithm;
import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.model.User;
import it.sii.challenge.valand.model.UserBusinessMatrix;
import it.sii.challenge.valand.utilities.CoupleObjectSimilarity;

import java.util.List;

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
		this.algorithm = new KNNAlgorithm(new SimilarityCalculator(), k);
				
		for (Review review : this.reviewsToTest){
			int prediction = this.predict(k, review);
			allPrediction += prediction + "\n";
		}	
		
		return allPrediction;
	}
	
	private int predict(int k, Review review){
		User user = this.matrix.getUser(review.getUserId());
		Business business = this.matrix.getBusiness(review.getBusinessId());
		
		if (user!=null && business!=null)
			return this.linearCombinationPrediction(review, user, business);
		else if (user!=null)
			return this.userBasedPrediction(review, user, business);
		else if (business!=null)
			return this.itemBasedPrediction(review, user);
		return AVERAGE_VALUE;
	}

	private int linearCombinationPrediction(Review review, User user, Business business) {
		List<CoupleObjectSimilarity<User>> userNeighborhood = this.algorithm.getNeighborHood(this.matrix, this.matrix.getUser(review.getUserId()), 
				this.matrix.getBusiness(review.getBusinessId()));
		List<CoupleObjectSimilarity<Business>> buisnessNeighborhood = this.algorithm.getNeighborHood(this.matrix, this.matrix.getBusiness(review.getBusinessId()),
				this.matrix.getUser(review.getUserId()));
		
		//Parametro che stabilisce dinamicamente quanto fidarsi delle predizioni
		int lambda = this.calculateLambda(userNeighborhood.size(), buisnessNeighborhood.size());
		
		int userPedict = userBasedPrediction(review, userNeighborhood, user, business);
		int buisnessPedict = itemBasedPrediction(review, buisnessNeighborhood, user);
		
		return lambda*userPedict + (1-lambda)*buisnessPedict;
	}

	private int calculateLambda(int userSize, int buisnessSize) {
		if (userSize < SOGLIA)
			return 0;
		if (buisnessSize < SOGLIA)
			return 1;
		
		return userSize / (userSize+buisnessSize); //us : us+bus = x : 1
	}

	private int userBasedPrediction(Review review, User user, Business business) {		
		List<CoupleObjectSimilarity<User>> neighborhood = this.algorithm.getNeighborHood(this.matrix, this.matrix.getUser(review.getUserId()), 
																		this.matrix.getBusiness(review.getBusinessId()));
		return this.userBasedPrediction(review, neighborhood, user, business);
	}

	private int itemBasedPrediction(Review review, User user) {
		List<CoupleObjectSimilarity<Business>> neighborhood = this.algorithm.getNeighborHood(this.matrix, this.matrix.getBusiness(review.getBusinessId()),
																		this.matrix.getUser(review.getUserId()));
		return this.itemBasedPrediction(review, neighborhood, user);
	}
	
	private int userBasedPrediction(Review review, List<CoupleObjectSimilarity<User>> neighborhood, User user, Business business) {
		if (neighborhood.size() < SOGLIA)
			return AVERAGE_VALUE;
		return this.algorithm.userBasedPrediction(neighborhood, review, user, business, this.matrix);
	}
	
	private int itemBasedPrediction(Review review, List<CoupleObjectSimilarity<Business>> neighborhood, User user) {
		if (neighborhood.size() < SOGLIA)
			return this.AVERAGE_VALUE;
		return this.algorithm.itemBasedPrediction(neighborhood, review, user, this.matrix);
	}

}
