package it.sii.challenge.valand.utilities;

import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserBusinessMatrix {
	//Map<UserId, Map<BusinessId, 
	private Map<String, Map<String, Integer>> matrix;
	
	
	/**
	 * Constructor
	 * @param reviewList 
	 * @param businessMaplist 
	 * @param userMap 
	 */
	public UserBusinessMatrix(Map<String, User> userMap, List<Review> reviewList){
			this.matrix = new HashMap<String, Map<String, Integer>>();
			
			for(String user : userMap.keySet()){
				Map<String, Integer> tempMap = new HashMap<String, Integer>();
				if(user == null)
					System.out.println("user è nullo");
				for(Review r : reviewList)
					if(r == null)
						System.out.println("R è nullo!!!");
					else if(r.getUserId().equals(user))
						tempMap.put(r.getBusinessId(), r.getStars());
				this.matrix.put(user, tempMap);
			}
	
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
	

	
	
	public String toString(){
		String output = "";
		
		for(String user : this.matrix.keySet()){
			output += user+":\t[";
			for(String business : this.matrix.get(user).keySet())
				output += business + " - "+ this.matrix.get(user).get(business).intValue() + " | ";
			output += "]\n";
		}

		return output;
	}
	
	
	
	
}
