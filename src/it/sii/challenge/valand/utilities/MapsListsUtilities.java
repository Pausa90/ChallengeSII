package it.sii.challenge.valand.utilities;

import it.sii.challenge.valand.model.UserBusinessMatrix;
import it.sii.challenge.valand.persistence.repository.MatrixRepository;
import it.sii.challenge.valand.persistence.repositoryImpl.MatrixRepositoryImpl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapsListsUtilities<T> {
	
	private MatrixRepository matrix_repo = new MatrixRepositoryImpl();

	public MapsListsUtilities(){}
	
	
	public Set<T> unionListOfKeySets(Map<T, Integer> map1, Map<T, Integer> map2){
		Set<T> result = new HashSet<T>();
		
		for(T s1 : map1.keySet())
			result.add(s1);
		for(T s2 : map2.keySet())
			result.add(s2);
		
		return result;
	}
	

	/** Metodi per i Test **/
	
//	public Map<String, Integer> insertion(){
//		Map<String, Integer> ratingsOfAUser = new HashMap<String, Integer>();
//		for (int i=0; i<50; i++){
//			String randomString = randomStringGenerator(10);
//			ratingsOfAUser.put(randomString, (int)(Math.random()*10));
//		}
//		return ratingsOfAUser;
//	}
//	
//	
//	public Map<String, Map<String, Integer>> multipleInsertionInMatrix(){
//		Map<String, Map<String, Integer>> matrix = new HashMap<String, Map<String, Integer>>();
//		for (int i=0; i<30; i++)
//			matrix.put(randomStringGenerator(5), insertion());
//		return matrix;
//	}
//	
//	public String randomStringGenerator(int lunghezza){
//		String risultato = "";
//		for(int i=0; i<lunghezza; i++)
//			risultato += (char)((int)((Math.random()*25)+97));
//		return risultato;
//	}

	

	
	
	
}
