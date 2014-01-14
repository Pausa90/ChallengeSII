package it.sii.challenge.valand.model;

import it.sii.challenge.valand.persistence.repository.BusinessRepository;
import it.sii.challenge.valand.persistence.repository.MatrixRepository;
import it.sii.challenge.valand.persistence.repository.UserRepository;
import it.sii.challenge.valand.persistence.repositoryImpl.BusinessRepositoryImpl;
import it.sii.challenge.valand.persistence.repositoryImpl.MatrixRepositoryImpl;
import it.sii.challenge.valand.persistence.repositoryImpl.UserRepositoryImpl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserBusinessMatrix {
	private Map<String, Map<String, Integer>> matrix;
	
	private boolean businessListGenerated = false;
	private List<String> businessListByAllUsers; 
	
	/**
	 * Constructor
	 * @param business 
	 * @param reviewList 
	 * @param businessMaplist 
	 * @param userMap 
	 */
	public UserBusinessMatrix(List<User> users, Map<String, Business> business, List<Review> reviews) {
		System.out.println("Crea matrice");
//		this.matrix = new HashMap<String, Map<String, Integer>>();
//		System.out.println("utenti:" + users.size());
//		for(User user : users){
//			Map<String, Integer> tempMap = new HashMap<String, Integer>();
//			for(Review review : reviews)
//				if(review.getUserId().equals(user.getId()))
//					tempMap.put(review.getBusinessId(), review.getStars());
//			this.matrix.put(user.getId(), tempMap);
//		}
		
		MatrixRepository matrix_repo = new MatrixRepositoryImpl();
		this.matrix = matrix_repo.getMatrix();
		System.out.println("fine matrice");
	}
	
	/**
	 * Costruttore vuoto per test
	 */
	public UserBusinessMatrix() {
		System.out.println("IN COSTRUTTORE VUOTO");
		System.out.println("Inizio Creazione Matrice");
		MatrixRepository matrix_repo = new MatrixRepositoryImpl();
		this.matrix = matrix_repo.getMatrix();
		System.out.println("fine matrice");
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
	
	public User getUserFromMatrix(String user_id){
		if(this.matrix.containsKey(user_id))
			return getUserFromUsers(user_id);
		else
			return null;
	}
	
	public Business getBusinessFromMatrix(String business_id){
		if (this.getBusinessListByAllUsers().contains(business_id))
			return getBusinessFromBusiness(business_id);
		else
			return null;
	}
	
	
	/** TODO: database **/
	public User getUserFromUsers(String user_id){
		UserRepository repo = new UserRepositoryImpl();
		return repo.findById(user_id);
	}	
	
	public Business getBusinessFromBusiness(String business_id){
		BusinessRepository repo = new BusinessRepositoryImpl();
		return repo.findById(business_id);
	}	
	/**			**/
	
	
	/**support methods**/
	public List<String> getBusinessListByAllUsers(){
		if(!businessListGenerated){
			this.businessListByAllUsers = new LinkedList<String>();
			for (String user : this.getMatrix().keySet())
				for(String business : this.getMatrix().get(user).keySet())
					if(!businessListByAllUsers.contains(business))
						businessListByAllUsers.add(business);
		}
		return businessListByAllUsers;
	}
	
	
	
	
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
	

	public void readTheMatrix(){
		for(String user : this.matrix.keySet()){
			System.out.print("User: " + user + " Business: ");
			Map<String, Integer> tempMap = this.matrix.get(user);
			for(String business : tempMap.keySet()){
				System.out.print("[ Id: " + business + " | rating: " + tempMap.get(business) + " ], " );
			}
			System.out.println("___");
		}
	}

	
	public boolean userInMatrix(String userId){
		return this.getMatrix().keySet().contains(userId);
	}
	
	public boolean businessInMatrix(String businessId){
		return this.getBusinessListByAllUsers().contains(businessId);
	}
	
	
}
