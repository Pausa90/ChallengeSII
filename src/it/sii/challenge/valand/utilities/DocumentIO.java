package it.sii.challenge.valand.utilities;

import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.Checkin;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.model.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.MalformedJsonException;

public class DocumentIO {
	private File businessFile;
	private File checkinFile;
	private File reviewFile;
	private File userFile;	
	
	private File outputFile;
	private File reviewsToTestFile;
	private File trueFile;

	public DocumentIO(String businessFileName, String checkinFileName, String reviewFileName, String userFileName, String testFileName, String outputFile, String trueFile){
		this.businessFile = newFileIstance(businessFileName);
		this.checkinFile = newFileIstance(checkinFileName);
		this.reviewFile = newFileIstance(reviewFileName);
		this.userFile = newFileIstance(userFileName);
		
		this.reviewsToTestFile = newFileIstance(testFileName);
		this.outputFile = newFileIstance(outputFile);
		this.setTrueFile(newFileIstance(trueFile));
	}

	private List<Business> getListBusinessFromFile(){		
		List<Business> result = new LinkedList<Business>();
		try {
			TypeToken<Business> token = new TypeToken<Business>(){};
			BufferedReader reader = new BufferedReader(new FileReader(this.businessFile));
			while(reader.ready())
				result.add((Business) new Gson().fromJson(reader.readLine(), token.getType()));
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Business file non trovato!");
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public List<User> getUsersFromFile(){
		List<User> result = new LinkedList<User>();
		try {
			TypeToken<User> token = new TypeToken<User>(){};
			BufferedReader reader = new BufferedReader(new FileReader(this.userFile));
			while(reader.ready())
				result.add((User) new Gson().fromJson(reader.readLine(), token.getType()));
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("User file non trovato!");
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public List<Checkin> getCheckinFromFile(){
		List<Checkin> result = new LinkedList<Checkin>();
		try {
			TypeToken<List<Checkin>> token = new TypeToken<List<Checkin>>(){};
			BufferedReader reader = new BufferedReader(new FileReader(this.checkinFile));
			while(reader.ready())
				result.add((Checkin) new Gson().fromJson(reader.readLine(), token.getType()));
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Checkin file non trovato!");
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public List<Review> getReviewFromFile(){
		List<Review> result = new LinkedList<Review>();
		try {
			TypeToken<Review> token = new TypeToken<Review>(){};
			BufferedReader reader = new BufferedReader(new FileReader(this.reviewFile));
			while(reader.ready()){
				String line = reader.readLine();
				try {
					result.add((Review) new Gson().fromJson(line, token.getType()));
				} catch (JsonSyntaxException e){
					e.printStackTrace();
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Review file non trovato!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	
	/**
	 * Methods to take maps from json file
	 */	
//	public Map<String, User> getUsersFromFile(){
//	Map<String, User> result = new HashMap<String, User>();
//		try {
//			for(User u : getListUsersFromFile()){
//				result.put(u.getId(), u);
//			}	
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
	
	
	public Map<String, Business> getBusinessFromFile(){
		Map<String, Business> result = new HashMap<String, Business>();
		try {
			for(Business b : getListBusinessFromFile()){
				result.put(b.getId(), b);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Review> getReviewsFromTest(){
		if (!reviewsToTestFile.isFile()){
			System.out.println("file file does not exist");
			return null;
		}			
		else{
			List<Review> reviews = new LinkedList<Review>();
			try{
				BufferedReader reader = new BufferedReader(new FileReader(reviewsToTestFile));
				String line = reader.readLine();
				while (line != null){
					if (!line.equals("")){
						Review review = this.extractReview(line);
						reviews.add(review);
					}
					line = reader.readLine();
				}
				reader.close();
				return reviews;
			} catch (IOException e){
				e.printStackTrace();
				return null;
			}
		}		
	}
	
	private Review extractReview(String line) {
		String[] splitted = line.split("\t");
		return new Review(splitted[1], splitted[0], -1);
	}
	
	public void save(String fileName, List<Review> reviewsToTest) {
		String toSave = this.getString(reviewsToTest);
		try {
			FileOutputStream file = new FileOutputStream(fileName);
			PrintStream stream = new PrintStream(file);
			stream.print(toSave);
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	public void saveStringInOutputFile(String s, FileWriter writer){
			try {
				writer.write(s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	
	public File getOutputFile(){
		return this.outputFile;
	}
	
	

	private String getString(List<Review> reviewsToTest) {
		String out = "";
		for (Review review : reviewsToTest)
			out += review.getStars() + "\n";		
		return out;
	}

	/**
	 * Metodo che verifica se il file system in uso è basato su Unix o su Windows.
	 * Utile per la gestione del path
	 */
	private boolean isUnixFileSystem() {
		if (System.getProperty("os.name").toLowerCase().contains("windows"))
			return false;			
		return true;
	}
	/**
	 * Crea un nuova istanza di file, occupandosi di capire il sistema operativo sottostante (Unix o Windows)
	 * @return
	 */
	private File newFileIstance(String abstractPath){
		final String completeTrainingBackupFilePath;
		if (this.isUnixFileSystem())
			completeTrainingBackupFilePath = System.getProperty("user.dir") + "/" + abstractPath;
		else
			completeTrainingBackupFilePath = System.getProperty("user.dir") + "\\" + abstractPath;
		return new File(completeTrainingBackupFilePath);
	}

	public File getTrueFile() {
		return trueFile;
	}

	public void setTrueFile(File trueFile) {
		this.trueFile = trueFile;
	}
}