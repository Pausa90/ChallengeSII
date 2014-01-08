package it.sii.challenge.valand.utilities;

import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.Checkin;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.model.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DocumentIO {
	private File businessFile;
	private File checkinFile;
	private File reviewFile;
	private File userFile;

	public DocumentIO(String businessFileName, String checkinFileName, String reviewFileName, String userFileName){
		this.businessFile = newFileIstance(businessFileName);
		this.checkinFile = newFileIstance(checkinFileName);
		this.reviewFile = newFileIstance(reviewFileName);
		this.userFile = newFileIstance(userFileName);
	}

	public Map<String,Business> getBusinessFromFile(){		
		try {
			TypeToken<Map<String, Business>> token = new TypeToken<Map<String, Business>>(){};
			BufferedReader reader = new BufferedReader(new FileReader(this.businessFile));
			return new Gson().fromJson(reader, token.getType());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<String,User> getUsersFromFile(){
		try {
			TypeToken<Map<String, User>> token = new TypeToken<Map<String, User>>(){};
			BufferedReader reader = new BufferedReader(new FileReader(this.userFile));
			return new Gson().fromJson(reader, token.getType());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Checkin> getCheckinFromFile(){
		try {
			TypeToken<List<Checkin>> token = new TypeToken<List<Checkin>>(){};
			BufferedReader reader = new BufferedReader(new FileReader(this.checkinFile));
			return new Gson().fromJson(reader, token.getType());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Review> getReviewFromFile(){
		try {
			TypeToken<List<Review>> token = new TypeToken<List<Review>>(){};
			BufferedReader reader = new BufferedReader(new FileReader(this.reviewFile));
			return new Gson().fromJson(reader, token.getType());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
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


}