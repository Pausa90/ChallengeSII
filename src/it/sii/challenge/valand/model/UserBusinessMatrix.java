package it.sii.challenge.valand.model;

import it.sii.challenge.valand.persistence.repository.BusinessRepository;
import it.sii.challenge.valand.persistence.repository.UserRepository;
import it.sii.challenge.valand.persistence.repositoryImpl.BusinessRepositoryImpl;
import it.sii.challenge.valand.persistence.repositoryImpl.UserRepositoryImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserBusinessMatrix {
	private Map<String, Map<String, Integer>> matrix;
	
	/**
	 * Constructor
	 * @param business 
	 * @param reviewList 
	 * @param businessMaplist 
	 * @param userMap 
	 */
	public UserBusinessMatrix(List<User> users, Map<String, Business> business, List<Review> reviews) {
		System.out.println("Crea matrice");
		this.matrix = new HashMap<String, Map<String, Integer>>();
		System.out.println("utenti:" + users.size());
		for(User user : users){
			Map<String, Integer> tempMap = new HashMap<String, Integer>();
			for(Review review : reviews)
				if(review.getUserId().equals(user.getId()))
					tempMap.put(review.getBusinessId(), review.getStars());
			this.matrix.put(user.getId(), tempMap);
		}
		System.out.println("fine matrice");
	}
	
	/**
	 * Costruttore vuoto per test
	 */
	public UserBusinessMatrix() {
		this.matrix = new HashMap<String, Map<String, Integer>>();
	}
		
	/**
	 * get entire matrix
	 */
	public Map<String, Map<String, Integer>> getMatrix(){
		return this.matrix;
	}
	
	/**
	 * A Row of the matrix. A row represent all ratings of a user, in a map where the key is the business_id
	 * @param user
	 * @return Map<String, Integer>
	 */
	public Map<String, Integer> getUserValutatedItems(String user){
		return this.getMatrix().get(user);
	}
	
	/**
	 * A Column of the matrix. A column represent all ratings of all users for a particular business
	 * @param business
	 * @return Map<String, Integer>
	 */
	public Map<String, Integer> getItemRatingsByAllUsers(String business){
		Map<String, Integer> result = new HashMap<String, Integer>();
		
		for (String user : this.getMatrix().keySet()){
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
		return this.getMatrix().get(user.getId()).get(business.getId());
	}
	
	public Integer getRatingByUserItem(String userId, String businessId){
		return this.getMatrix().get(userId).get(businessId);
	}
	
	
	/** TODO: database **/
	public User getUser(String user_id){
		UserRepository repo = new UserRepositoryImpl();
		return repo.findById(user_id);
	}	
	
	public Business getBusiness(String business_id){
		BusinessRepository repo = new BusinessRepositoryImpl();
		return repo.findById(business_id);
	}	
	/**			**/
	
	public String toString(){
		String output = "";
		boolean first = true;
		
		for(String user : this.matrix.keySet()){
			output += user+":\t[";
			for(String business : this.matrix.get(user).keySet())
				if (first){
					output += business + " - "+ this.matrix.get(user).get(business).intValue();
					first = false;
				}
				else output += " | " + business + " - "+ this.matrix.get(user).get(business).intValue();
			output += "]\n";
			first = true;
		}

		return output;
	}
	
	
	
	
}
