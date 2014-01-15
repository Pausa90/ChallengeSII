package it.sii.challenge.valand.model;


public class Review {
	
	private String business_id;
	private String user_id;
	private int stars;
	/*
	private String text;
	private String date; //this is a formatted date: yyyy-mm-dd in strptime notation
	private Map<String, Integer> votes;
	*/
	
	/**
	 * Constructors
	 */
	public Review(String businessId, String userId, int stars) {
		super();
		this.business_id = businessId;
		this.user_id = userId;
		this.stars = stars;
	}	
	
	
	/**
	 * Getters & Setters
	 */
	
	public String getBusinessId() {
		return business_id;
	}
	
	public void setBusinessId(String businessId) {
		this.business_id = businessId;
	}
	
	public String getUserId() {
		return user_id;
	}
	
	public void setUserId(String userId) {
		this.user_id = userId;
	}
	
	public int getStars() {
		return stars;
	}
	
	public void setStars(int stars) {
		this.stars = stars;
	}

	public boolean equals(Object obj){
		Review review = (Review) obj;
		return this.user_id.equals(review.getUserId()) && this.business_id.equals(review.getBusinessId());
	}
	
	public String toString() {
		return "BusinessID: " + this.business_id + ", UserID: "+ this.user_id + ", Stars: "+ stars;
	}

}
