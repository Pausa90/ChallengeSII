package it.sii.challenge.valand.model;


public class User {
	
	private String user_id;
	private int review_count;
	private double average_stars;
	/*
	private String name; //firstname
	private Map<String, Integer> votes;
	*/
	
	
	/**
	 * Constructors
	 */
	public User(String id, int reviewCount, double averageStars) {
		super();
		this.user_id = id;
		this.review_count = reviewCount;
		this.average_stars = averageStars;
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (user_id == null) {
			if (other.user_id != null)
				return false;
		} else if (!user_id.equals(other.user_id))
			return false;
		return true;
	}
	
	
	
}
