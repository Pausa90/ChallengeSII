package it.sii.challenge.valand.logic;

import it.sii.challenge.valand.utilities.DocumentIO;
import it.sii.challenge.valand.utilities.UserBusinessMatrix;

public class Classifier {
	
	private UserBusinessMatrix matrix;

	public Classifier(String userFile, String businessFile, String reviewFile, String checkinFile) {
		DocumentIO documentIO = new DocumentIO(businessFile, checkinFile, reviewFile, userFile);
		this.matrix = new UserBusinessMatrix(documentIO.getUsersFromFile(), documentIO.getReviewFromFile());
	
		System.out.println(this.matrix.toString());
	}
	
	
	
	public UserBusinessMatrix getMatrix(){
		return this.matrix;
	}
	
	
	
}
