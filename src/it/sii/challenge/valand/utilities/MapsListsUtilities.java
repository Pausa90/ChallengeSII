package it.sii.challenge.valand.utilities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapsListsUtilities {

	public MapsListsUtilities(){}
	
	public Set<String> unionListOfKeySets(Map<String, Integer> map1, Map<String, Integer> map2){
		Set<String> result = new HashSet<String>();
		
		for(String s1 : map1.keySet())
			result.add(s1);
		for(String s2 : map2.keySet())
			result.add(s2);
		
		return result;
	}
	

	/** Metodi per i Test **/
	
	public Map<String, Integer> insertion(){
		Map<String, Integer> ratingsOfAUser = new HashMap<String, Integer>();
		for (int i=0; i<50; i++){
			String randomString = randomStringGenerator(10);
			ratingsOfAUser.put(randomString, (int)(Math.random()*10));
		}
		return ratingsOfAUser;
	}
	
	
	public Map<String, Map<String, Integer>> multipleInsertionInMatrix(){
		Map<String, Map<String, Integer>> matrix = new HashMap<String, Map<String, Integer>>();
		for (int i=0; i<30; i++)
			matrix.put(randomStringGenerator(5), insertion());
		return matrix;
	}
	
	public String randomStringGenerator(int lunghezza){
		String risultato = "";
		for(int i=0; i<lunghezza; i++)
			risultato += (char)((int)((Math.random()*25)+97));
		return risultato;
	}
	
	
	
}
