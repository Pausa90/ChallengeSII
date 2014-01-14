package it.sii.challenge.valand.persistence.repository;

import it.sii.challenge.valand.model.Business;

import java.util.Map;

public interface MatrixRepository {
	
	
	/**
	 * Insertions
	 */
	
	public boolean insertUserRatings(Map<String, Integer> userRatings);
	public boolean insertBusinessRatings(Map<String, Integer> userRatings);
	
	
	/**
	 * Searches
	 */
	
	/**
	 * @return a column of the matrix
	 */
	public Map<String, Integer> findBusinessRatings();
	
	/**
	 * @return a row of the matrix
	 */	
	public Map<String, Integer> findUserRatings();
	 
	public Business findARating(String u_id, String b_id);
	
	public Map<String, Map<String, Integer>> getMatrix();

	
}
