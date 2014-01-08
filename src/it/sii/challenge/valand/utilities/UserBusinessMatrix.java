package it.sii.challenge.valand.utilities;

import java.util.HashMap;
import java.util.Map;

public class UserBusinessMatrix {
	private Map<String, Map<String, Integer>> matrix;
	
	/**
	 * Constructor
	 */
	public UserBusinessMatrix(){
		this.matrix = new HashMap<String, Map<String, Integer>>();
	}
	
	
	/**
	 * get entire matrix
	 */
	public Map<String, Map<String, Integer>> getMatrix(){
		return this.matrix;
	}
	
	/**
	 * @param userKey
	 * @return Map<String, Integer>
	 * @return A Row of the matrix. A row represent all ratings of a user, in a map where the key is the business_id
	 */
	public Map<String, Integer> getUserValutatedItems(String userKey){
		return this.getMatrix().get(userKey);
	}
	
	/**
	 * @param businessKey
	 * @return Map<String, Integer>
	 * @return A Column of the matrix. A column represent all ratings of all users for a particular business
	 */
	public Map<String, Integer> getItemRatingsByAllUsers(String businessKey){
		Map<String, Integer> result = new HashMap<String, Integer>();
		
		for (String userId : this.getMatrix().keySet()){
			if(this.getMatrix().get(userId).containsKey(businessKey))
				result.put(userId, this.getMatrix().get(userId).get(businessKey));
		}
		
		return result;
	}
	
	/**
	 * @param userKey, businessKey
	 * @return Integer
	 * @return The Rating of a User for a particular Business
	 */
	public Integer getRatingByUserItem(String userKey, String businessKey){
		return this.getMatrix().get(userKey).get(businessKey);
	}
	
	
	
}
