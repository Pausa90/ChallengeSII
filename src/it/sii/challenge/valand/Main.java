package it.sii.challenge.valand;

import it.sii.challenge.valand.logic.Classifier;
import it.sii.challenge.valand.logic.Predictor;
import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.model.User;
import it.sii.challenge.valand.utilities.DocumentIO;

import java.util.List;
import java.util.Map;

public class Main {

	public static void main(String[] args) {
		String trainingPath = "dataset/";

		String testFile = trainingPath+"test.dat";
		String outputFile = trainingPath+"output.dat";
		
		//Training Files
		String userFile = trainingPath+"user.json";
		String businessFile = trainingPath+"business.json";
		String reviewFile = trainingPath+"review_training.json";
		String checkinFile = trainingPath+"checkin.json";		
		
		DocumentIO documentIO = new DocumentIO(businessFile, checkinFile, reviewFile, userFile, testFile);
		List<User> users = documentIO.getUsersFromFile();
		Map<String, Business> business = documentIO.getBusinessFromFile();
		List<Review> reviews = documentIO.getReviewFromFile();
	//	List<Checkin> checkins = documentIO.getCheckinFromFile();
		
		Classifier classifier = new Classifier(users, business, reviews);
		System.out.println(classifier.getMatrix());
		
//		
//		List<Review> reviewsToTest = documentIO.getReviewsFromTest();
//		Predictor predictor = new Predictor(classifier.getMatrix(), reviewsToTest);
//	
//		int k = 3;
//		predictor.startPrediction(k);
//		
//		documentIO.save(outputFile, reviewsToTest);
	}

}
