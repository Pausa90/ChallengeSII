package it.sii.challenge.valand.logic;

import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.model.User;
import it.sii.challenge.valand.model.UserBusinessMatrix;

import java.util.List;
import java.util.Map;

public class Classifier {
	
	private UserBusinessMatrix matrix;
	
	
	public Classifier(){
		this.matrix = new UserBusinessMatrix();
	}
	
	public Classifier(List<User> users, Map<String, Business> business, List<Review> reviews) {
		this.matrix = new UserBusinessMatrix(users, business, reviews);
	}

	public UserBusinessMatrix getMatrix(){
		return this.matrix;
	}
	
	
	
}
