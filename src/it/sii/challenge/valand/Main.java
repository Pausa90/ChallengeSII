package it.sii.challenge.valand;

import it.sii.challenge.valand.logic.Classifier;
import it.sii.challenge.valand.logic.Predictor;
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

		String userFile = trainingPath+"user.json";
		String businessFile = trainingPath+"business.json";
		String reviewFile = trainingPath+"review_training.json";
		String checkinFile = trainingPath+"checkin.json";		
		
		DocumentIO documentIO = new DocumentIO(businessFile, checkinFile, reviewFile, userFile, testFile, outputFile);
//		populateDB(documentIO);
		
		Classifier classifier = new Classifier();
		//classifier.getMatrix().readTheMatrix();
		
		List<Review> reviewsToTest = documentIO.getReviewsFromTest();
		Predictor predictor = new Predictor(classifier.getMatrix(), reviewsToTest);
	
		int k = 3;
		predictor.startPrediction(k, documentIO);
		
		long endTime = System.nanoTime();
		printTemporalInformation(startTime, endTime);
		
	}
	
	public static void printTemporalInformation(long startTime, long endTime){
		long toSeconds = 1000000000;
		int minutes = (int)((endTime - startTime)/(toSeconds*60));
		int seconds = (int)(((endTime - startTime)/toSeconds)-(minutes*60));
		System.out.println("The program took "+ minutes + " minutes and " + seconds + " seconds"); 
	}

	public static void populateDB(DocumentIO documentIO){
		System.out.println("Start population");
		BusinessRepository b_repo = new BusinessRepositoryImpl();
		UserRepository u_repo = new UserRepositoryImpl();
		ReviewRepository r_repo = new ReviewRepositoryImpl();
		
		Map<String, Business> business = documentIO.getBusinessFromFile();
		List<Business> businessList = new LinkedList<Business>();
		for (Business b : business.values())
			businessList.add(b);
		
		List<User> users = documentIO.getUsersFromFile();
		List<Review> reviews = documentIO.getReviewFromFile();
		
		inferInformation(reviews, users, businessList);
		
		b_repo.insertList(businessList);
		u_repo.insertList(users);
		r_repo.insertList(reviews);
		System.out.println("Population is done");
	}

	private static void inferInformation(List<Review> reviews, List<User> users, List<Business> businesses) {
		List<User> newUsers = new LinkedList<User>();
		List<Business> newBusinesses = new LinkedList<Business>();
		
		for (Review review : reviews){
			//Se non è presente inferiamo l'utente
			if (!users.contains(review.getUserId())){
				User newUser = new User(review.getUserId());
				if (newUsers.contains(newUser))
					newUser = newUsers.get(newUsers.indexOf(newUser));
				newUser.setAverageStars(newUser.getAverageStars()+review.getStars());
				newUser.setReviewCount(newUser.getReviewCount()+1);
			}
			//Se non è presente inferiamo il business
			if (!businesses.contains(review.getBusinessId())){
				Business newBusiness = new Business(review.getBusinessId());
				if (newBusinesses.contains(newBusiness))
					newBusiness = newBusinesses.get(newBusinesses.indexOf(newBusiness));
				newBusiness.setStars(newBusiness.getStars()+review.getStars());
				newBusiness.setReviewCount(newBusiness.getReviewCount()+1);				
			}
		}
		
		//Sistemiamo le medie
		for (User user : newUsers)
			user.setAverageStars(user.getAverageStars()/user.getReviewCount());
		for (Business business : newBusinesses)
			business.setStars(business.getStars()/business.getReviewCount());
		
		users.addAll(newUsers);
		businesses.addAll(newBusinesses);
	}
	
}
