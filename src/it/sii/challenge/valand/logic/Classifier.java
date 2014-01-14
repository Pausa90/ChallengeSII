package it.sii.challenge.valand.logic;

import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.model.User;
import it.sii.challenge.valand.model.UserBusinessMatrix;
import it.sii.challenge.valand.persistence.repository.BusinessRepository;
import it.sii.challenge.valand.persistence.repository.ReviewRepository;
import it.sii.challenge.valand.persistence.repository.UserRepository;
import it.sii.challenge.valand.persistence.repositoryImpl.BusinessRepositoryImpl;
import it.sii.challenge.valand.persistence.repositoryImpl.ReviewRepositoryImpl;
import it.sii.challenge.valand.persistence.repositoryImpl.UserRepositoryImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Classifier {
	
	private UserBusinessMatrix matrix;
	private boolean generatedMatrix = false;
	
	
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
