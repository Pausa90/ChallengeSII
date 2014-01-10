package it.sii.challenge.valand;

import it.sii.challenge.valand.logic.Classifier;

public class Main {

	public static void main(String[] args) {
		String trainingPath = "dataset/";

		String testFile = trainingPath+"test.json";
		String outputFile = trainingPath+"output.dat";
		
		//Training Files
		String userFile = trainingPath+"user_test.json";
		String businessFile = trainingPath+"business_test.json";
		String reviewFile = trainingPath+"review_test.json";
		String checkinFile = trainingPath+"checkin_test.json";
		
		Classifier classifier = new Classifier(userFile, businessFile, reviewFile, checkinFile);
		
		
		
	}

}
