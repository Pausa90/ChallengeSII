package it.sii.challenge.valand.model;

import java.util.List;

public class Business {

	private String id;
	private String name;
	private List<String> neighborhoods;
	private double stars;
	private int reviewCount;
	private List<String> categories;
	/*
	private String address;
	private String city;
	private String state;
	private String latitude;
	private String longitude;
	private boolean open; //indicating if the structure is still working or failed.
	*/
	
	/**
	 * Constructors
	 */
	
	public Business(String id, String name, List<String> neighborhoods,
			double stars, int reviewCount, List<String> categories) {
		super();
		this.id = id;
		this.name = name;
		this.neighborhoods = neighborhoods;
		this.stars = stars;
		this.reviewCount = reviewCount;
		this.categories = categories;
	}
		
	public Business(String id, String name, double stars) {
		super();
		this.id = id;
		this.name = name;
		this.stars = stars;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getNeighborhoods() {
		return neighborhoods;
	}
	public void setNeighborhoods(List<String> neighborhoods) {
		this.neighborhoods = neighborhoods;
	}
	public double getStars() {
		return stars;
	}
	public void setStars(double stars) {
		this.stars = stars;
	}
	public int getReviewCount() {
		return reviewCount;
	}
	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
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
		Business other = (Business) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
