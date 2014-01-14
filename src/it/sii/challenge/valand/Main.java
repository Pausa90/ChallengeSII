package it.sii.challenge.valand;

import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.model.User;
import it.sii.challenge.valand.persistence.repository.BusinessRepository;
import it.sii.challenge.valand.persistence.repository.ReviewRepository;
import it.sii.challenge.valand.persistence.repository.UserRepository;
import it.sii.challenge.valand.persistence.repositoryImpl.BusinessRepositoryImpl;
import it.sii.challenge.valand.persistence.repositoryImpl.ReviewRepositoryImpl;
import it.sii.challenge.valand.persistence.repositoryImpl.UserRepositoryImpl;
import it.sii.challenge.valand.utilities.DocumentIO;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
		
		DocumentIO documentIO = new DocumentIO(businessFile, checkinFile, reviewFile, userFile, testFile);
		List<User> users = documentIO.getUsersFromFile();
		Map<String, Business> business = documentIO.getBusinessFromFile();
		
		List<Business> businessList = new LinkedList<Business>();
		for (Business b : business.values())
			businessList.add(b);
		
		List<Review> reviews = documentIO.getReviewFromFile();
	//	List<Checkin> checkins = documentIO.getCheckinFromFile();
		
		//Popolazione DB
		BusinessRepository b_repo = new BusinessRepositoryImpl();
		UserRepository u_repo = new UserRepositoryImpl();
		ReviewRepository r_repo = new ReviewRepositoryImpl();
		
//		b_repo.insertList(businessList);
//		u_repo.insertList(users);
		r_repo.insertList(reviews);
		
		System.out.println("Everything's done");
		
//		Classifier classifier = new Classifier(users, business, reviews);
//		System.out.println(classifier.getMatrix());
		
//		
//		List<Review> reviewsToTest = documentIO.getReviewsFromTest();
//		Predictor predictor = new Predictor(classifier.getMatrix(), reviewsToTest);
//	
//		int k = 3;
//		predictor.startPrediction(k);
//		
//		documentIO.save(outputFile, reviewsToTest);
		
		
		
		
		long endTime = System.nanoTime();
		long toMinute = Long.parseLong("60000000000");
		System.out.println("The program took "+(endTime - startTime)/toMinute + " minutes"); 
		
	}

}
