package it.sii.challenge.valand.logic;

import it.sii.challenge.valand.logic.algorithm.ClassificationAlgorithm;
import it.sii.challenge.valand.logic.algorithm.KNNAlgorithm;
import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.model.User;
import it.sii.challenge.valand.model.UserBusinessMatrix;
import it.sii.challenge.valand.utilities.CoupleObjectSimilarity;
import it.sii.challenge.valand.utilities.DocumentIO;

import java.io.FileWriter;
import java.io.IOException;
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
	
	public void startPrediction(int k, DocumentIO doc_io){		
		this.algorithm = new KNNAlgorithm(new SimilarityCalculator(), k);
		
		FileWriter writerOutput;
		try {
			writerOutput = new FileWriter(doc_io.getOutputFile());
			for (Review review : this.reviewsToTest){
				int prediction = this.predict(k, review);
				System.out.println("Predizione: " + prediction);
				writerOutput.write(prediction + "\n");
			}	
	
			writerOutput.flush();
			writerOutput.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	
	private int predict(int k, Review review){
		User user = this.matrix.getUserFromMatrix(review.getUserId());
		Business business = this.matrix.getBusinessFromMatrix(review.getBusinessId());
		
		if (user!=null && business!=null){
			return this.linearCombinationPrediction(review, user, business);
		}
		else if (user!=null){
			business = this.matrix.getBusinessFromBusiness(review.getBusinessId());
			return this.userBasedPrediction(review, user, business);
		}
		else if (business!=null){
			user = this.matrix.getUserFromUsers(review.getUserId());
			if(user == null)
				return AVERAGE_VALUE;
			return this.itemBasedPrediction(review, user);
		}
		return AVERAGE_VALUE;
	}

	private int linearCombinationPrediction(Review review, User user, Business business) {
		List<CoupleObjectSimilarity<User>> userNeighborhood = this.algorithm.getNeighborHood(this.matrix, this.matrix.getUserFromMatrix(review.getUserId()), 
				this.matrix.getBusinessFromMatrix(review.getBusinessId()));
		List<CoupleObjectSimilarity<Business>> buisnessNeighborhood = this.algorithm.getNeighborHood(this.matrix, this.matrix.getBusinessFromMatrix(review.getBusinessId()),
				this.matrix.getUserFromMatrix(review.getUserId()));
		
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
		List<CoupleObjectSimilarity<User>> neighborhood = this.algorithm.getNeighborHood(this.matrix, this.matrix.getUserFromMatrix(review.getUserId()), 
																		this.matrix.getBusinessFromMatrix(review.getBusinessId()));
		return this.userBasedPrediction(review, neighborhood, user, business);
	}

	private int itemBasedPrediction(Review review, User user) {
		Business b = this.matrix.getBusinessFromBusiness(review.getBusinessId());
		List<CoupleObjectSimilarity<Business>> neighborhood = this.algorithm.getNeighborHood(this.matrix, b, user);
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
