package it.sii.challenge.valand.logic;

import java.util.List;
import java.util.Map;

import it.sii.challenge.valand.utilities.DocumentIO;
import it.sii.challenge.valand.utilities.UserBusinessMatrix;
import it.sii.challenge.valand.model.User;

public class Classifier {
	
	private UserBusinessMatrix matrix;
	private Map<String, User> users;

	public Classifier(String userFile, String businessFile, String reviewFile, String checkinFile) {
		DocumentIO documentIO = new DocumentIO(businessFile, checkinFile, reviewFile, userFile);
		this.users = documentIO.getUsersFromFile();
		
		this.matrix = new UserBusinessMatrix(this.users, documentIO.getReviewFromFile());
	
		System.out.println(this.matrix.toString());
	}
	
	
	
	public UserBusinessMatrix getMatrix(){
		return this.matrix;
	}
	
	
	
}
