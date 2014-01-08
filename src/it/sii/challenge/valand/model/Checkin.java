package it.sii.challenge.valand.model;

import java.util.Map;

public class Checkin {

	private String businessId;
	private Map<String, Integer> checkinInfo;

	
	
	/**
	 * Getters & Setters
	 */
	
	
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public Map<String, Integer> getCheckinInfo() {
		return checkinInfo;
	}
	public void setCheckinInfo(Map<String, Integer> checkinInfo) {
		this.checkinInfo = checkinInfo;
	}
	
	
	
}
