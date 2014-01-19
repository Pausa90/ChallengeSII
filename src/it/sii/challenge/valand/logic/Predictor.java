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

	private UserBusinessMatrix matrix;
	private List<Review> reviewsToTest;
	private ClassificationAlgorithm algorithm;
	private final int BUSINESS_REVIEW_COUNT_TRESHOLD = 10; //Stabilisce l'affidabilità minima di un business. 
	private final int USERS_REVIEW_COUNT_TRESHOLD = 20; //Stabilisce l'affidabilità minima di uno user.
	private final int COMMON_TRESHOLD = 1; 
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

	/**
	 * Metodo di avvio della predizione
	 * @param doc_io: oggetto che si interfaccia con la lettura/scrittura su file
	 */
	public void startPrediction(DocumentIO doc_io){		
		this.algorithm = new KNNAlgorithm();

		FileWriter writerOutput;
		try {
			writerOutput = new FileWriter(doc_io.getOutputFile());
			for (Review review : this.reviewsToTest){
				int prediction = this.predict(review);
				if (prediction > 5)
					review.setStars(5);
				else if (prediction < 1){
					review.setStars(1);
				}
				else review.setStars(prediction);
				writerOutput.write(review.getStars() + "\n");
			}	
			writerOutput.flush();
			writerOutput.close();

		} catch (IOException e) {
			e.printStackTrace();
		}		

	}

	/**
	 * Effettua la predizione della singola review
	 * @param review
	 * @return stars
	 */
	private int predict(Review review){
		User user = this.matrix.getUserFromMatrix(review.getUserId());
		Business business = this.matrix.getBusinessFromMatrix(review.getBusinessId());

		if (user!=null && business!=null)
			return this.linearCombinationPrediction(review, user, business);
		else if (business != null)
			return approximate(business.getStars());
		else if (user != null)
			return approximate(user.getAverageStars());
		return AVERAGE_VALUE;
	}

	/**
	 * Effettua la combinazione lineare tra la user-based e l'item-based,
	 * considerando opportunamente (e dinamicamente) la fedeltà delle due.
	 * @param review
	 * @param user
	 * @param business
	 * @return stars
	 */
	private int linearCombinationPrediction(Review review, User user, Business business) {
		PredictionList<User> userNeighborhood = this.getUserNeighborhood(user, review);
		PredictionList<Business> buisnessNeighborhood = this.getBusinessNeighborhood(business, review);

		//Parametro che stabilisce dinamicamente quanto fidarsi delle predizioni
		double lambda = this.calculateLambda(userNeighborhood.getCommonsValue(), buisnessNeighborhood.getCommonsValue());

		int userPredict;
		int buisnessPredict;
		if (lambda == -1)
			return AVERAGE_VALUE;
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

	/**
	 * Approssima un double all'intero più vicino
	 * @param predicted
	 * @return
	 */
	private int approximate(double predicted) {
		int casted = (int) predicted;
		if (predicted-casted > 0.5)
			return casted+1;
		else
			return casted;
	}

	/**
	 * Calcola il neighborhood relativo ad un utente
	 * @param user
	 * @param review
	 * @return
	 */
	private PredictionList<User> getUserNeighborhood(User user, Review review){
		if (user.getReviewCount() < USERS_REVIEW_COUNT_TRESHOLD){
			return new PredictionList<User>();
		}
		return this.algorithm.getNeighborHood(this.matrix, this.matrix.getUserFromMatrix(review.getUserId()), 
				this.matrix.getBusinessFromMatrix(review.getBusinessId()));
	}

	/**
	 * Calcola il neighborhood relativo ad un business
	 * @param business
	 * @param review
	 * @return
	 */
	private PredictionList<Business> getBusinessNeighborhood(Business business, Review review){
		if (business.getReviewCount() < BUSINESS_REVIEW_COUNT_TRESHOLD){
			return new PredictionList<Business>();
		}
		return this.algorithm.getNeighborHood(this.matrix, this.matrix.getBusinessFromMatrix(review.getBusinessId()),
				this.matrix.getUserFromMatrix(review.getUserId()));
	}

	/**
	 * Calcola il valore del lambda secondo la seguente proporzione: user : user+business = x : 1
	 * @param user
	 * @param buisness
	 * @return
	 */
	private double calculateLambda(int user, int buisness) {
		if (user < COMMON_TRESHOLD && buisness < COMMON_TRESHOLD)
			return -1;
		if (user < COMMON_TRESHOLD)
			return 0.;
		if (buisness < COMMON_TRESHOLD)
			return 1.;		
		return user / (double) (user+buisness); //us : us+bus = x : 1
	}

	/**
	 * Wrapper per l'avvio della user-based prediction
	 * @param review
	 * @param neighborhood
	 * @param user
	 * @param business
	 * @return
	 */
	private int userBasedPrediction(Review review, PredictionList<User> neighborhood, User user, Business business) {
		return this.algorithm.userBasedPrediction(neighborhood, review, user, business, this.matrix);
	}

	/**
	 * Wrapper per l'avvio della item-based prediction
	 * @param review
	 * @param neighborhood
	 * @param user
	 * @return
	 */
	private int itemBasedPrediction(Review review, PredictionList<Business> neighborhood, User user) {
		return this.algorithm.itemBasedPrediction(neighborhood, review, user, this.matrix);
	}

}
