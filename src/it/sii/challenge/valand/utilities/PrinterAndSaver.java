package it.sii.challenge.valand.utilities;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Classe che ha il compito di stampare a schermo le stringhe in ingresso e di memorizzarle all'interno di un file
 * @author andrea
 */

public class PrinterAndSaver {
	
	private String backup;
	
	public PrinterAndSaver(){
		this.backup = "";
	}
	
	public void printAndSave(String s){
		this.backup += s + "\n";
		System.out.println(s);
	}
	
	public void addToBackup(String s){
		this.backup += s + "\n";
	}
	
	public void save(String fileName){
		this.save(fileName, this.backup);
	}
	
	public void save(String fileName, String toSave){
		try {
			FileOutputStream file = new FileOutputStream(fileName);
			PrintStream stream = new PrintStream(file);
			stream.print(toSave);
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void clean(){
		this.backup = "";
	}
	


}
