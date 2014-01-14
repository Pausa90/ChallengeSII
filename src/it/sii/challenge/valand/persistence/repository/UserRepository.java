package it.sii.challenge.valand.persistence.repository;

import it.sii.challenge.valand.model.User;

import java.util.List;

public interface UserRepository {
	
	
	/**
	 * Insertions
	 */
	
	public boolean insert(User u);	
	
	public boolean insertList(List<User> listUser);
	
	/**
	 * Searches
	 */
	
	public List<User> findAll();
	 
	public User findById(String id);

	
}
