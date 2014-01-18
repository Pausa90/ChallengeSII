package it.sii.challenge.valand.logic;

import it.sii.challenge.valand.logic.algorithm.ClassificationAlgorithm;
import it.sii.challenge.valand.logic.algorithm.KNNAlgorithm;
import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.model.User;
import it.sii.challenge.valand.model.UserBusinessMatrix;
import it.sii.challenge.valand.utilities.DocumentIO;
import it.sii.challenge.valand.utilities.PredictionList;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Predictor {
	
	/**
	 * Best:
	 * BUSINESS_REVIEW_COUNT_TRESHOLD = 0;
	 * USERS_REVIEW_COUNT_TRESHOLD = 100;
	 * COMMON_TRESHOLD = 50;
	 * NEIGHBORHOOD_TRESHOLD = 5;
	 */

	private UserBusinessMatrix matrix;
	private List<Review> reviewsToTest;
	private ClassificationAlgorithm algorithm;
	private final int BUSINESS_REVIEW_COUNT_TRESHOLD = 30;
	private final int USERS_REVIEW_COUNT_TRESHOLD = 100; //min count to calculate neighborhood
	private final int COMMON_TRESHOLD = 50;
	private final int NEIGHBORHOOD_TRESHOLD = 5; //min size of neighborhood
	private final int AVERAGE_VALUE = 4;
	
	

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
	
	public void startPrediction(DocumentIO doc_io){		
		this.algorithm = new KNNAlgorithm(new SimilarityCalculator());
		
		System.out.println("Inizio Predizione");		
		FileWriter writerOutput;
		try {
			writerOutput = new FileWriter(doc_io.getOutputFile());
			int i = 1;
			for (Review review : this.reviewsToTest){
				int prediction = this.predict(review);
				if (prediction > 5)
					review.setStars(5);
				else if (prediction < 1){
					review.setStars(1);
				}
				else review.setStars(prediction);
				i++;
				writerOutput.write(review.getStars() + "\n");
				//if (i > 1000) break;
			}	
			writerOutput.flush();
			writerOutput.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
	}

	private int predict(Review review){
		User user = this.matrix.getUserFromMatrix(review.getUserId());
		Business business = this.matrix.getBusinessFromMatrix(review.getBusinessId());
		
		if (user!=null && business!=null)
			return this.linearCombinationPrediction(review, user, business);
		return AVERAGE_VALUE;
	}
	
	private int linearCombinationPrediction(Review review, User user, Business business) {
		PredictionList<User> userNeighborhood = this.getUserNeighborhood(user, review);
		PredictionList<Business> buisnessNeighborhood = this.getBusinessNeighborhood(business, review);
		
		//Parametro che stabilisce dinamicamente quanto fidarsi delle predizioni
		//double lambda = this.calculateLambda(userNeighborhood.getCommonsValue(), buisnessNeighborhood.getCommonsValue());
		double lambda = this.calculateLambda(userNeighborhood.size(), buisnessNeighborhood.size());

		int userPredict;
		int buisnessPredict;
		if (lambda == 0)
			userPredict = 0;
		else
			userPredict = userBasedPrediction(review, userNeighborhood, user, business);
		if (lambda == 1)
			buisnessPredict = 0;
		else
			buisnessPredict = itemBasedPrediction(review, buisnessNeighborhood, user);
		
		return approximate(lambda*userPredict + (1-lambda)*buisnessPredict);
	}
	
	private int approximate(double predicted) {
		int casted = (int) predicted;
		if (predicted-casted > 0.5)
			return casted+1;
		else
			return casted;
	}
	
	private PredictionList<User> getUserNeighborhood(User user, Review review){
		if (user.getReviewCount() < USERS_REVIEW_COUNT_TRESHOLD){
			return new PredictionList<User>();
		}
		return this.algorithm.getNeighborHood(this.matrix, this.matrix.getUserFromMatrix(review.getUserId()), 
				this.matrix.getBusinessFromMatrix(review.getBusinessId()));
	}
	
	private PredictionList<Business> getBusinessNeighborhood(Business business, Review review){
		if (business.getReviewCount() < BUSINESS_REVIEW_COUNT_TRESHOLD){
			return new PredictionList<Business>();
		}
		return this.algorithm.getNeighborHood(this.matrix, this.matrix.getBusinessFromMatrix(review.getBusinessId()),
				this.matrix.getUserFromMatrix(review.getUserId()));
	}

	private double calculateLambda(int user, int buisness) {
		if (user < COMMON_TRESHOLD)
			return 0.;
		if (buisness < COMMON_TRESHOLD)
			return 1.;		
		return user / (double) (user+buisness); //us : us+bus = x : 1
	}
	
	private int userBasedPrediction(Review review, PredictionList<User> neighborhood, User user, Business business) {
		if (neighborhood.size() < NEIGHBORHOOD_TRESHOLD){
			return AVERAGE_VALUE;
		}
		return this.algorithm.userBasedPrediction(neighborhood, review, user, business, this.matrix);
	}

	private int itemBasedPrediction(Review review, PredictionList<Business> neighborhood, User user) {
		if (neighborhood.size() < NEIGHBORHOOD_TRESHOLD){
			return this.AVERAGE_VALUE;
		}
		return this.algorithm.itemBasedPrediction(neighborhood, review, user, this.matrix);
	}

}
