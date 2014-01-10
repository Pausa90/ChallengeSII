package it.sii.challenge.valand.model;

import java.util.Map;

public class Checkin {

	private String business_id;
	private Map<String, Integer> checkin_info;

	
	/**
	 * Getters & Setters
	 */
	
	
	public String getBusinessId() {
		return business_id;
	}
	public void setBusinessId(String businessId) {
		this.business_id = businessId;
	}
	public Map<String, Integer> getCheckinInfo() {
		return checkin_info;
	}
	public void setCheckinInfo(Map<String, Integer> checkinInfo) {
		this.checkin_info = checkinInfo;
	}
	
	
	
}
