package it.sii.challenge.valand.logic.algorithm;

import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.model.User;
import it.sii.challenge.valand.model.UserBusinessMatrix;
import it.sii.challenge.valand.persistence.repository.ReviewRepository;
import it.sii.challenge.valand.persistence.repositoryImpl.ReviewRepositoryImpl;
import it.sii.challenge.valand.utilities.CommonValues;
import it.sii.challenge.valand.utilities.CoupleObjectSimilarity;
import it.sii.challenge.valand.utilities.PredictionList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * KNN è utilizzato per lavorare sulla matrice user-item,
 * interfacciandosi con il database.
 */

public class KNNAlgorithm extends ClassificationAlgorithm {

	public KNNAlgorithm() {
		super();
	}

	
	@Override
	public PredictionList<User> getNeighborHood(UserBusinessMatrix matrix, User user, Business business) {			
		ReviewRepository repo = new ReviewRepositoryImpl();
		PredictionList<User> neighborhood = new PredictionList<User>();
		List<User> users = repo.getNeighborhood(user, business);
		if (users.size() == 0) {
			return neighborhood;
		}
		
		double similarity;
		for (User u : users){
			similarity = this.getSimilarity(user, u);
			if (similarity > SIMILARITY_TRESHOLD)
				neighborhood.add(new CoupleObjectSimilarity<User>(u, similarity), u.getCountSameBusiness());
		}
		return neighborhood;
	}

	private double getSimilarity(User user, User u) {
			return 1 - (Math.abs(user.getAverageStars() - u.getAverageStars())/4.);
	}

	/** 
	 * Return the item-based Neighborhood
	 */
	@Override
	public PredictionList<Business> getNeighborHood(UserBusinessMatrix matrix, Business business, User user) {
		ReviewRepository repo = new ReviewRepositoryImpl();
		PredictionList<Business> neighborhood = new PredictionList<Business>();
		List<Business> businesses = repo.getNeighborhood(business, user);
		if (businesses.size() == 0) {
			return neighborhood;
		}
		
		double similarity;
		for (Business b : businesses){	
				similarity = this.getSimilarity(business, b);
				if (similarity > SIMILARITY_TRESHOLD)
					neighborhood.add(new CoupleObjectSimilarity<Business>(b, similarity), b.getCountSameUsers());
		}
		return neighborhood;
	}

	private double getSimilarity(Business business, Business b) {
		return 1 - (Math.abs(business.getStars() - b.getStars())/4.);
	}

	@Override
	public int itemBasedPrediction(PredictionList<Business> neighborhood, Review review, User user, UserBusinessMatrix matrix) {
		Collections.sort(neighborhood.getList(), new MaxSimilarity<Business>());

		double predictNumerator = 0;
		double predictDenominator = 0;
		Business bs;
		for (CoupleObjectSimilarity<Business> b : neighborhood.getList()){
			bs = b.getObject();
			predictNumerator += bs.getCountSameUsers() * (matrix.getRatingByUserItem(user.getId(), bs.getId()) - user.getAverageStars());
			predictDenominator += bs.getCountSameUsers();
		} 
		
		this.setStars(review, user.getAverageStars() + predictNumerator/predictDenominator);
		return review.getStars();
	}

	@Override
	public int userBasedPrediction(PredictionList<User> neighborhood, Review review, User user, Business business, UserBusinessMatrix matrix) {
		Collections.sort(neighborhood.getList(), new MaxSimilarity<User>());
		double predictNumerator = 0;
		double predictDenominator = 0;
		User us;
		for (CoupleObjectSimilarity<User> u : neighborhood.getList()){
			us = u.getObject();
			predictNumerator += us.getCountSameBusiness() * (matrix.getRatingByUserItem(us.getId(), business.getId()) - us.getAverageStars());
			predictDenominator += us.getCountSameBusiness();
		}

		this.setStars(review, user.getAverageStars() + predictNumerator/predictDenominator);
		return review.getStars();
	}

	/**
	 * Imposta il voto approssimando il double all'intero più vicino
	 * @param review
	 * @param predicted
	 */
	private void setStars(Review review, double predicted) {
		int casted = (int) predicted;
		if (predicted-casted > 0.5)
			review.setStars(casted+1);
		else
			review.setStars(casted);
	}
	
}

/**
 * Metodo di comparazione basato sulla distanza tra due object
 * @author andrea e valerio
 * @param <T>
 *
 */
class MaxSimilarity<T> implements Comparator<CoupleObjectSimilarity<T>>{
	
	public int compare(CoupleObjectSimilarity<T> c1, CoupleObjectSimilarity<T> c2) {
		CommonValues nc1 = (CommonValues) c1.getObject();
		CommonValues nc2 = (CommonValues) c2.getObject();
		
		return Double.compare(nc2.getCommonValues(),nc1.getCommonValues());
	}	
}
