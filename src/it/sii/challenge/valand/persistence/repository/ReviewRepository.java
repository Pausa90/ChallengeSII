package it.sii.challenge.valand.persistence.repository;

import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.model.User;

import java.util.List;

public interface ReviewRepository {
	
	
	/**
	 * Insertions
	 */
	
	public boolean insert(Review r);	
	
	boolean insertList(List<Review> listReview);

	/**
	 * Searches
	 */
	
	public List<Review> findAll();
	 
	public Review findById(String b_id, String u_id);
	
	public List<User> getNeighborhood(User user, Business business, int treshold);


	
}
