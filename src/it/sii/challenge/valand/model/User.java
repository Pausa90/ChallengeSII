package it.sii.challenge.valand.model;


public class User {
	
	private String id;
	private int reviewCount;
	private double averageStars;
	/*
	private String name; //firstname
	private Map<String, Integer> votes;
	*/
	
	/**
	 * Constructors
	 */
	public User(String id, int reviewCount, double averageStars) {
		super();
		this.id = id;
		this.reviewCount = reviewCount;
		this.averageStars = averageStars;
	}
	
	
	/**
	 * Getters & Setters
	 */
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getReviewCount() {
		return reviewCount;
	}
	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}
	public double getAverageStars() {
		return averageStars;
	}
	public void setAverageStars(double averageStars) {
		this.averageStars = averageStars;
	}

	/**
	 * Equals & Hashcode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
