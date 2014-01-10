package it.sii.challenge.valand.utilities;

import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class UserBusinessMatrixTest {

	private UserBusinessMatrix matrixContainer;
	private Map<String, Map<String, Integer>> matrix;
	
	@Before
	public void Setup(){
		this.matrixContainer = new UserBusinessMatrix();
		this.matrix = matrixContainer.getMatrix();
	} 
	

	
	
	@Test
	public void ConstructorTest() {
		assertTrue(this.matrix.isEmpty());
	}

	
	
	
	
	
	@Test
	public void insertionTest(){
		this.matrix = new MapsListsUtilities().multipleInsertionInMatrix();
		
		assertTrue(this.matrix.keySet().size() == 30);
		String firstKey = this.matrix.keySet().iterator().next();
		assertTrue(firstKey.length() == 5);
		Iterator<String> i = this.matrix.get(firstKey).keySet().iterator();
		assertTrue(i.next().length() == 10);
		assertTrue(i.next().length() == 10);
		
		for(String s : this.matrix.keySet()){
			for(String s2 : this.matrix.get(s).keySet()){
				System.out.print(s+"\t");
				System.out.println(s2+"\t"+this.matrix.get(s).get(s2));
			}
		}
	}
	
	
	
}
