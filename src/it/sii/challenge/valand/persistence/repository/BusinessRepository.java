package it.sii.challenge.valand.persistence.repository;

import it.sii.challenge.valand.model.Business;

import java.util.List;

public interface BusinessRepository {
	
	
	/**
	 * Insertions
	 */
	
	public boolean insert(Business b);	

	public boolean insertList(List<Business> listBusiness);	
	
	
	/**
	 * Searches
	 */
	
	public List<Business> findAll();
	 
	public Business findById(String id);

	public boolean insertCategories(List<Business> businessList);

	
}
