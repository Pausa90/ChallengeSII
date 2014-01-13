package it.sii.challenge.valand.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserBusinessMatrix {
	private Map<User, Map<Business, Integer>> matrix;
	
	/**
	 * Constructor
	 * @param business 
	 * @param reviewList 
	 * @param businessMaplist 
	 * @param userMap 
	 */
	public UserBusinessMatrix(List<User> users, Map<String, Business> business, List<Review> reviews) {
		System.out.println("Crea matrice");
		this.matrix = new HashMap<User, Map<Business, Integer>>();
		System.out.println("utenti:" + users.size());
		for(User user : users){
			Map<Business, Integer> tempMap = new HashMap<Business, Integer>();
			for(Review review : reviews)
				if(review.getUserId().equals(user.getId()))
					tempMap.put(business.get(review.getBusinessId()), review.getStars());
			this.matrix.put(user, tempMap);
		}
		System.out.println("fine matrice");
	}
	
	/**
	 * Costruttore vuoto per test
	 */
	public UserBusinessMatrix() {
		this.matrix = new HashMap<User, Map<Business, Integer>>();
	}
		
	/**
	 * get entire matrix
	 */
	public Map<User, Map<Business, Integer>> getMatrix(){
		return this.matrix;
	}
	
	/**
	 * A Row of the matrix. A row represent all ratings of a user, in a map where the key is the business_id
	 * @param user
	 * @return Map<String, Integer>
	 */
	public Map<Business, Integer> getUserValutatedItems(User user){
		return this.getMatrix().get(user);
	}
	
	/**
	 * A Column of the matrix. A column represent all ratings of all users for a particular business
	 * @param business
	 * @return Map<String, Integer>
	 */
	public Map<User, Integer> getItemRatingsByAllUsers(Business business){
		Map<User, Integer> result = new HashMap<User, Integer>();
		
		for (User user : this.getMatrix().keySet()){
			if(this.getMatrix().get(user).containsKey(business))
				result.put(user, this.getMatrix().get(user).get(business));
		}
		return result;
	}
	
	/**
	 * The Rating of a User for a particular Business
	 * @param user, business
	 * @return Integer
	 */
	public Integer getRatingByUserItem(User user, Business business){
		return this.getMatrix().get(user).get(business);
	}
	
	
	/** TODO: database **/
	public User getUser(String user_id){
		for (User user : this.matrix.keySet())
			if (user.getId().equals(user_id))
				return user;
		return null;		
	}	
	
	public Business getBusiness(String business_id){
		for (User user : this.matrix.keySet())
			for (Business business : this.matrix.get(user).keySet())
				if (business.getId().equals(business_id))
					return business;
		return null;		
	}	
	/**			**/
	
	public String toString(){
		String output = "";
		boolean first = true;
		
		for(User user : this.matrix.keySet()){
			output += user.getId()+":\t[";
			for(Business business : this.matrix.get(user).keySet())
				if (first){
					output += business.getId() + " - "+ this.matrix.get(user).get(business).intValue();
					first = false;
				}
				else output += " | " + business.getId() + " - "+ this.matrix.get(user).get(business).intValue();
			output += "]\n";
			first = true;
		}

		return output;
	}
	
	
	
	
}
