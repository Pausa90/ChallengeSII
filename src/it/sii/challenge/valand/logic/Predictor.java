package it.sii.challenge.valand.logic;

import it.sii.challenge.valand.logic.algorithm.ClassificationAlgorithm;
import it.sii.challenge.valand.logic.algorithm.KNNAlgorithm;
import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.model.User;
import it.sii.challenge.valand.model.UserBusinessMatrix;
import it.sii.challenge.valand.utilities.CoupleObjectSimilarity;
import it.sii.challenge.valand.utilities.DocumentIO;
import it.sii.challenge.valand.utilities.PredictionList;
import it.sii.challenge.valand.utilities.PrinterAndSaver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
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
	private final int BUSINESS_REVIEW_COUNT_TRESHOLD = 0;
	private final int USERS_REVIEW_COUNT_TRESHOLD = 100; //min count to calculate neighborhood
	private final int COMMON_TRESHOLD = 30;
	private final int NEIGHBORHOOD_TRESHOLD = 5; //min size of neighborhood
	private final int AVERAGE_VALUE = 4;
	private PrinterAndSaver printer;
	
	

	public Predictor(UserBusinessMatrix matrix, List<Review> reviewsToTest) {
		this.setMatrix(matrix);
		this.reviewsToTest = reviewsToTest;
		this.printer = new PrinterAndSaver();
	}

	public UserBusinessMatrix getMatrix() {
		return matrix;
	}

	public void setMatrix(UserBusinessMatrix matrix) {
		this.matrix = matrix;
	}
	
	public void startPrediction(int k, DocumentIO doc_io){		
		this.algorithm = new KNNAlgorithm(new SimilarityCalculator(), k, this.printer);
		
		System.out.println("Inizio Predizione");		
		FileWriter writerOutput;
		try {
			writerOutput = new FileWriter(doc_io.getOutputFile());
			int i = 1;
			for (Review review : this.reviewsToTest){
				this.printer.addToBackup("/*********************************** NUOVA REVIEW *******************************************/");
				int prediction = this.predict(k, review);
				if (prediction > 5)
					review.setStars(5);
				else if (prediction < 1){
					System.out.println("review sotto l'1");
					review.setStars(1);
				}
				else review.setStars(prediction);
				this.printer.addToBackup("Predizione " + i + ": " + review.getStars());
				i++;
				writerOutput.write(review.getStars() + "\n");
				if (i > 1000) break;
//				if (i > 1000) break;
			}	
			writerOutput.flush();
			writerOutput.close();
			this.printer.save("stampe");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	/*
//	private int predict(int k, Review review){
//		User user = this.matrix.getUserFromMatrix(review.getUserId());
//		Business business = this.matrix.getBusinessFromMatrix(review.getBusinessId());
//		
//		if (user!=null && business!=null){
//			return this.linearCombinationPrediction(review, user, business);
//		}
//		else if (user!=null){
//			business = this.matrix.getBusinessFromBusiness(review.getBusinessId());
//			return this.userBasedPrediction(review, user, business);
//		}
//		else if (business!=null){
//			user = this.matrix.getUserFromUsers(review.getUserId());
//			if(user == null)
//				return AVERAGE_VALUE;
//			return this.itemBasedPrediction(review, user);
//		}
//		return AVERAGE_VALUE;
//	}
*/
	private int predict(int k, Review review){
		User user = this.matrix.getUserFromMatrix(review.getUserId());
		Business business = this.matrix.getBusinessFromMatrix(review.getBusinessId());
		
		if (user!=null && business!=null)
			return this.linearCombinationPrediction(review, user, business);
		return AVERAGE_VALUE;
	}
	
	private int linearCombinationPrediction(Review review, User user, Business business) {
		this.printer.addToBackup("Inizio Combinazione Lineare");
		PredictionList<User> userNeighborhood = this.getUserNeighborhood(user, review);
		PredictionList<Business> buisnessNeighborhood = this.getBusinessNeighborhood(business, review);
//		
//		//Parametro che stabilisce dinamicamente quanto fidarsi delle predizioni
//		double lambda = this.calculateLambda(userNeighborhood.size(), buisnessNeighborhood.size());
		double lambda = this.calculateLambda(userNeighborhood.getCommonsValue(), buisnessNeighborhood.getCommonsValue());
		this.printer.addToBackup("lambda = " + lambda + " (userNeigh=" + userNeighborhood.size() + "  businessNeig=" + buisnessNeighborhood.size());
//		
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
		this.printer.addToBackup("userbased: " + userPredict + ", businessbased: " + buisnessPredict);
		
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
			this.printer.addToBackup("user sotto la treshold (" + user.getReviewCount() + ")");
			return new PredictionList<User>();
		}
		return this.algorithm.getNeighborHood(this.matrix, this.matrix.getUserFromMatrix(review.getUserId()), 
				this.matrix.getBusinessFromMatrix(review.getBusinessId()), this.printer);
	}
	
	private PredictionList<Business> getBusinessNeighborhood(Business business, Review review){
		if (business.getReviewCount() < BUSINESS_REVIEW_COUNT_TRESHOLD){
			this.printer.addToBackup("business sotto la treshold (" + business.getReviewCount() + ")");
			return new PredictionList<Business>();
		}
		return this.algorithm.getNeighborHood(this.matrix, this.matrix.getBusinessFromMatrix(review.getBusinessId()),
				this.matrix.getUserFromMatrix(review.getUserId()));
	}

//	private double calculateLambda(int userSize, int buisnessSize) {//TODO provare a prendere getCountSameUsers
//		if (userSize < NEIGHBORHOOD_TRESHOLD)
//			return 0.;
//		if (buisnessSize < NEIGHBORHOOD_TRESHOLD)
//			return 1.;
//		
//		return userSize / (double) (userSize+buisnessSize); //us : us+bus = x : 1
//	}

	private double calculateLambda(int user, int buisness) {//TODO provare a prendere getCountSameUsers
		if (user < COMMON_TRESHOLD)
			return 0.;
		if (buisness < COMMON_TRESHOLD)
			return 1.;
		
		return user / (double) (user+buisness); //us : us+bus = x : 1
	}
	
/*
//	private int userBasedPrediction(Review review, User user, Business business) {		
//		List<CoupleObjectSimilarity<User>> neighborhood = this.algorithm.getNeighborHood(this.matrix, this.matrix.getUserFromMatrix(review.getUserId()), 
//																		this.matrix.getBusinessFromMatrix(review.getBusinessId()));
//		return this.userBasedPrediction(review, neighborhood, user, business);
//	}

//	private int itemBasedPrediction(Review review, User user) {
//		Business b = this.matrix.getBusinessFromBusiness(review.getBusinessId());
//		List<CoupleObjectSimilarity<Business>> neighborhood = this.algorithm.getNeighborHood(this.matrix, b, user);
//		return this.itemBasedPrediction(review, neighborhood, user);
//	}
	*/
	private int userBasedPrediction(Review review, PredictionList<User> neighborhood, User user, Business business) {
		if (neighborhood.size() < NEIGHBORHOOD_TRESHOLD){
			this.printer.addToBackup("userNeigh troppo scarsa (" + neighborhood.size() + ")");
			return AVERAGE_VALUE;
		}
		return this.algorithm.userBasedPrediction(neighborhood, review, user, business, this.matrix);
	}

	private int itemBasedPrediction(Review review, PredictionList<Business> neighborhood, User user) {
		if (neighborhood.size() < NEIGHBORHOOD_TRESHOLD){
			this.printer.addToBackup("itemNeigh troppo scarsa (" + neighborhood.size() + ")");
			return this.AVERAGE_VALUE;
		}
		return this.algorithm.itemBasedPrediction(neighborhood, review, user, this.matrix);
	}

}
