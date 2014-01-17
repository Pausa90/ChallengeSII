package it.sii.challenge.valand;

import it.sii.challenge.valand.logic.Classifier;
import it.sii.challenge.valand.logic.Predictor;
import it.sii.challenge.valand.logic.Statistic;
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

import org.omg.CORBA.RepositoryIdHelper;

public class Main {

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		
		String trainingPath = "dataset/";

		String testFile = trainingPath+"review_test.dat";
		String outputFile = trainingPath+"output.dat";
		String trueFile = trainingPath+"review_realratings.dat";

		String userFile = trainingPath+"user.json";
		String businessFile = trainingPath+"business.json";
		String reviewFile = trainingPath+"review_training.json";
		String checkinFile = trainingPath+"checkin.json";	
		
		DocumentIO documentIO = new DocumentIO(businessFile, checkinFile, reviewFile, userFile, testFile, outputFile, trueFile);

//		populateDB(documentIO);
		
		Classifier classifier = new Classifier();
//		classifier.getMatrix().readTheMatrix();
		
		List<Review> reviewsToTest = documentIO.getReviewsFromTest();
		Predictor predictor = new Predictor(classifier.getMatrix(), reviewsToTest);
//	
		int k = 5;
		predictor.startPrediction(k, documentIO);
		
//		ReviewRepository r_repo = new ReviewRepositoryImpl();
//		BusinessRepository b_repo = new BusinessRepositoryImpl();
//		UserRepository u_repo = new UserRepositoryImpl();
//		List<Review> reviews = r_repo.findAll();
//		List<User> users = u_repo.findAll();
//		List<Business> businesses = b_repo.findAll();
//		fixDatabase(reviews, users, businesses);
//		
//		
		
		Statistic statistics = new Statistic(startTime);
		statistics.printMAE(documentIO.getOutputFile(), documentIO.getTrueFile());
		
		long endTime = System.nanoTime();
		statistics.printTemporalInformation(endTime);
		
	}
	


	public static void populateDB(DocumentIO documentIO){
		System.out.println("Start population");
		BusinessRepository b_repo = new BusinessRepositoryImpl();
		UserRepository u_repo = new UserRepositoryImpl();
		ReviewRepository r_repo = new ReviewRepositoryImpl();
	
		List<Business> businessList = documentIO.getListBusinessFromFile();
		List<User> users = documentIO.getUsersFromFile();
		List<Review> reviews = documentIO.getReviewFromFile();
		
		System.out.println("presi da file user, business, review");
		System.out.println("Business.size: " + businessList.size());
		System.out.println("User.size: " + users.size());
		System.out.println("Review.size: " + reviews.size());
		
		inferInformation(reviews, users, businessList);
		System.out.println("Inferite informazioni...");
		System.out.println("Business.size: " + businessList.size());
		System.out.println("User.size: " + users.size());
		System.out.println("Review.size: " + reviews.size());
		
		
		
		b_repo.insertList(businessList);
		u_repo.insertList(users);
		r_repo.insertList(reviews);
		System.out.println("Population is done");
	}

	private static void inferInformation(List<Review> reviews, List<User> users, List<Business> businesses) {
		List<User> newUsers = new LinkedList<User>();
		List<Business> newBusinesses = new LinkedList<Business>();
		System.out.println("Infer information..");
		System.out.println("Scandisco le review...");
		User newUser;
		Business newBusiness;
		for (Review review : reviews){
			newUser = new User(review.getUserId());
			newBusiness = new Business(review.getBusinessId());
			//Se non è presente inferiamo l'utente
			if (!users.contains(newUser)){
				if (newUsers.contains(newUser)){
					newUser = newUsers.get(newUsers.indexOf(newUser));
					newUser.setAverageStars(newUser.getAverageStars()+review.getStars());
					newUser.setReviewCount(newUser.getReviewCount()+1);	
				}
				else{
					newUser.setAverageStars(newUser.getAverageStars()+review.getStars());
					newUser.setReviewCount(newUser.getReviewCount()+1);
					newUsers.add(newUser);
				}
				
			}
			//Se non è presente inferiamo il business
			if (!businesses.contains(newBusiness)){
				if (newBusinesses.contains(newBusiness)){
					newBusiness = newBusinesses.get(newBusinesses.indexOf(newBusiness));
					newBusiness.setStars(newBusiness.getStars()+review.getStars());
					newBusiness.setReviewCount(newBusiness.getReviewCount()+1);								
				}
				newBusiness.setStars(newBusiness.getStars()+review.getStars());
				newBusiness.setReviewCount(newBusiness.getReviewCount()+1);
				newBusinesses.add(newBusiness);
			}
		}
		System.out.println("sistemo le medie...");
		//Sistemiamo le medie
		for (User user : newUsers)
			user.setAverageStars(user.getAverageStars()/user.getReviewCount());
		for (Business business : newBusinesses)
			business.setStars(business.getStars()/business.getReviewCount());
		
		System.out.println("In totale: ");
		System.out.println("Users nuovi: " + newUsers.size());
		System.out.println("Business nuovi: " + newBusinesses.size());
		
		users.addAll(newUsers);
		businesses.addAll(newBusinesses);
	}
	
	//Metodo provvisorio
	public static void fixDatabase(List<Review> reviews, List<User> users, List<Business> businesses){
		ReviewRepositoryImpl r_repo = new ReviewRepositoryImpl();		
		List<Review> toDelete = new LinkedList<Review>();
		int tenere = 0;
		int eliminare = 0;
		for (Review review : reviews){
			if (!users.contains(new User(review.getUserId())) || !businesses.contains(new Business(review.getBusinessId())) ){
				toDelete.add(review);
				eliminare++;
			}
			else{
				tenere++;
			}
		}
		System.out.println("eliminare:" + eliminare + ", tenere:" + tenere);
		r_repo.delete(toDelete);
	}
	
}
