package it.sii.challenge.valand.model;


public class Review {
	
	private String businessId;
	private String userId;
	private double stars;
	/*
	private String text;
	private String date; //this is a formatted date: yyyy-mm-dd in strptime notation
	private Map<String, Integer> votes;
	*/
	/**
	 * Constructors
	 */
	public Review(String businessId, String userId, double stars) {
		super();
		this.businessId = businessId;
		this.userId = userId;
		this.stars = stars;
	}	
	
	
	/**
	 * Getters & Setters
	 */
	
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public double getStars() {
		return stars;
	}
	public void setStars(double stars) {
		this.stars = stars;
	}

	
	

}
