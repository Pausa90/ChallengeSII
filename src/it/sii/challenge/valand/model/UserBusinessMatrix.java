package it.sii.challenge.valand.model;

import it.sii.challenge.valand.persistence.repository.BusinessRepository;
import it.sii.challenge.valand.persistence.repository.MatrixRepository;
import it.sii.challenge.valand.persistence.repository.UserRepository;
import it.sii.challenge.valand.persistence.repositoryImpl.BusinessRepositoryImpl;
import it.sii.challenge.valand.persistence.repositoryImpl.MatrixRepositoryImpl;
import it.sii.challenge.valand.persistence.repositoryImpl.UserRepositoryImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Rappresenta la matrice (sparsa) user-item
 * @author andrea e valerio
 *
 */

public class UserBusinessMatrix {
	private Map<String, Map<String, Integer>> matrix;
	private boolean businessListGenerated = false;
	private List<String> businessListByAllUsers; 

	public UserBusinessMatrix(List<User> users, Map<String, Business> business, List<Review> reviews) {
		MatrixRepository matrix_repo = new MatrixRepositoryImpl();
		this.matrix = matrix_repo.getMatrix();
		System.out.println("fine matrice");
	}

	public UserBusinessMatrix() {
		MatrixRepository matrix_repo = new MatrixRepositoryImpl();
		this.matrix = matrix_repo.getMatrix();
	}

	public Map<String, Map<String, Integer>> getMatrix(){
		return this.matrix;
	}

	/**
	 * Riporta una riga della matrice, ossia tutti i business votati dall'utente 
	 * @param user
	 * @return Map<String, Integer>, (business_id, stars)
	 */
	public Map<String, Integer> getUserValutatedItems(String user){
		return this.getMatrix().get(user);
	}

	/**
	 * Riporta una colonna della matrice, ossia tutti gli utenti che hanno votato il particolare business
	 * @param business
	 * @return Map<String, Integer>, (user_id, stars)
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
	 * Riporta il voto attribuito da un utente ad un particolare business
	 * @param user, business
	 * @return Integer
	 */
	public Integer getRatingByUserItem(User user, Business business){
		return this.getMatrix().get(user.getId()).get(business.getId());
	}

	/**
	 * Riporta il voto attribuito da un utente ad un particolare business
	 * @param userId
	 * @param businessId
	 * @return
	 */
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

	public User getUserFromUsers(String user_id){
		UserRepository repo = new UserRepositoryImpl();
		return repo.findById(user_id);
	}	

	public Business getBusinessFromBusiness(String business_id){
		BusinessRepository repo = new BusinessRepositoryImpl();
		return repo.findById(business_id);
	}	

	private List<String> getBusinessListByAllUsers(){
		MatrixRepositoryImpl repo = new MatrixRepositoryImpl();
		if(!this.businessListGenerated){
			this.businessListByAllUsers = repo.findAllVotatedItems();
			this.businessListGenerated = true;
		}
		return this.businessListByAllUsers;
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

	/**
	 * Stampa la matrice
	 */
	public void readTheMatrix(){
		boolean first = true;
		for(String user : this.matrix.keySet()){
			System.out.print("User: " + user + " Business: ");
			Map<String, Integer> tempMap = this.matrix.get(user);
			for(String business : tempMap.keySet()){				
				if (first){
					System.out.print("[ Id: " + business + " | rating: " + tempMap.get(business) + " ]" );
					first = false;
				}
				else 
					System.out.print(", [ Id: " + business + " | rating: " + tempMap.get(business) + " ]" );
			}
			first = true;
			System.out.println();
		}
	}

}
