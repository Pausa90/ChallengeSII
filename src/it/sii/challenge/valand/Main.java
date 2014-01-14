package it.sii.challenge.valand;

import it.sii.challenge.valand.logic.Classifier;
import it.sii.challenge.valand.logic.Predictor;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.utilities.DocumentIO;

import java.io.FileWriter;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		
		String trainingPath = "dataset/";

		String testFile = trainingPath+"review_test.dat";
		String outputFile = trainingPath+"output.dat";
		
		//Training Files
		String userFile = trainingPath+"user.json";
		String businessFile = trainingPath+"business.json";
		String reviewFile = trainingPath+"review_training.json";
		String checkinFile = trainingPath+"checkin.json";		
		
		DocumentIO documentIO = new DocumentIO(businessFile, checkinFile, reviewFile, userFile, testFile, outputFile);
//		List<User> users = documentIO.getUsersFromFile();
//		Map<String, Business> business = documentIO.getBusinessFromFile();
//		
//		List<Business> businessList = new LinkedList<Business>();
//		for (Business b : business.values())
//			businessList.add(b);
//		
//		List<Review> reviews = documentIO.getReviewFromFile();
	//	List<Checkin> checkins = documentIO.getCheckinFromFile();

		
/**		
		//Popolazione DB
		BusinessRepository b_repo = new BusinessRepositoryImpl();
		UserRepository u_repo = new UserRepositoryImpl();
		ReviewRepository r_repo = new ReviewRepositoryImpl();
		
//		b_repo.insertList(businessList);
//		u_repo.insertList(users);
//		r_repo.insertList(reviews);
//		System.out.println("Everything's done");

**/		
		
		Classifier classifier = new Classifier();
//		//System.out.println(classifier.getMatrix().toString());
//		System.out.println(classifier.getMatrix().getRatingByUserItem(user, business));
		classifier.getMatrix().readTheMatrix();
		
//		
		List<Review> reviewsToTest = documentIO.getReviewsFromTest();
		Predictor predictor = new Predictor(classifier.getMatrix(), reviewsToTest);
//	
		System.out.println("HO CREATO IL PREDITTORE!");
		int k = 3;
		predictor.startPrediction(k, documentIO);
//		
//		documentIO.save(outputFile, reviewsToTest);

		
		long endTime = System.nanoTime();
		long toSeconds = 1000000000;
		int minutes = (int)((endTime - startTime)/(toSeconds*60));
		int seconds = (int)(((endTime - startTime)/toSeconds)-(minutes*60));
		System.out.println("The program took "+ minutes + " minutes and " + seconds + " seconds"); 
		
	}

}
