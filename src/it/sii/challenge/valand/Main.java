package it.sii.challenge.valand;

import java.util.List;
import java.util.Map;

import it.sii.challenge.valand.logic.Classifier;
import it.sii.challenge.valand.logic.Predictor;
import it.sii.challenge.valand.logic.algorithm.ClassificationAlgorithm;
import it.sii.challenge.valand.logic.algorithm.KNNAlgorithm;
import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.Checkin;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.model.User;
import it.sii.challenge.valand.utilities.DocumentIO;
import it.sii.challenge.valand.utilities.PrinterAndSaver;

public class Main {

	public static void main(String[] args) {
		String trainingPath = "dataset/";

		String testFile = trainingPath+"test.dat";
		String outputFile = trainingPath+"output.dat";
		
		//Training Files
		String userFile = trainingPath+"user_test.json";
		String businessFile = trainingPath+"business_test.json";
		String reviewFile = trainingPath+"review_test.json";
		String checkinFile = trainingPath+"checkin_test.json";		
		
		PrinterAndSaver outputManager = new PrinterAndSaver();
		DocumentIO documentIO = new DocumentIO(businessFile, checkinFile, reviewFile, userFile, testFile);
		List<User> users = documentIO.getUsersFromFile();
		Map<String, Business> business = documentIO.getBusinessFromFile();
		List<Review> reviews = documentIO.getReviewFromFile();
	//	List<Checkin> checkins = documentIO.getCheckinFromFile();
		List<Review> reviewToTest = documentIO.getReviewsFromTest();
		
		Classifier classifier = new Classifier(users, business, reviews);
		
		List<Review> reviewsToTest = documentIO.getReviewsFromTest();
		Predictor predictor = new Predictor(classifier.getMatrix(), reviewsToTest);
	
		int k = 3;
		predictor.startPrediction(k);
		
		
	}

}
