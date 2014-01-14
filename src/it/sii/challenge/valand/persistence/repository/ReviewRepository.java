package it.sii.challenge.valand.persistence.repository;

import it.sii.challenge.valand.model.Review;

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


	
}
