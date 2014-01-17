package it.sii.challenge.valand.model;


public class User {
	
	private String user_id;
	private int review_count;
	private double average_stars;
	private int countSameBusiness;
	
	/**
	 * Constructors
	 */
	public User(String id, int reviewCount, double averageStars) {
		this.user_id = id;
		this.review_count = reviewCount;
		this.average_stars = averageStars;
	}
	
	public User(String id, int reviewCount, double averageStars, int countSameBusiness) {
		this.user_id = id;
		this.review_count = reviewCount;
		this.average_stars = averageStars;
		this.setCountSameBusiness(countSameBusiness);
	}
	
	
	public User(String id){
		this(id, 0, 0);
	}
	
	/**
	 * Getters & Setters
	 */
	public String getId() {
		return user_id;
	}
	
	public void setId(String id) {
		this.user_id = id;
	}
	
	public int getReviewCount() {
		return review_count;
	}
	
	public void setReviewCount(int reviewCount) {
		this.review_count = reviewCount;
	}
	
	public double getAverageStars() {
		return average_stars;
	}
	
	public void setAverageStars(double averageStars) {
		this.average_stars = averageStars;
	}

	/**
	 * Equals & Hashcode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user_id == null) ? 0 : user_id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		User user = (User) obj;
		return this.getId().equals(user.getId());
	}
	
	public String toString(){
		return "UserID: " + this.user_id;
	}

	public int getCountSameBusiness() {
		return countSameBusiness;
	}

	public void setCountSameBusiness(int countSameBusiness) {
		this.countSameBusiness = countSameBusiness;
	}
	
	
}
